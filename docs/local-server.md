# Локальный тестовый сервер

Этот документ описывает, как поднять на своём компьютере (Windows) локальный сервер Paper 26.3 и проверить, что плагин `FreedomBot` из модуля `plugin` загружается и работает.

Сервер нужен только для проверки плагина. Он слушает `127.0.0.1` и недоступен из сети: брандмауэр и проброс портов настраивать не нужно.

> **Paper или Leaf.** На боевом сервере FREEDOM используется [Leaf](https://www.leafmc.one/) — форк Paper. Плагин написан на чистом Paper API, поэтому одинаково работает на Paper, Purpur и Leaf. Для локальной проверки достаточно Paper. Чтобы проверить плагин в условиях, максимально близких к боевым, скачайте Leaf 26.3 с официального сайта ([leafmc.one/download](https://www.leafmc.one/download)) или из [GitHub Releases](https://github.com/Winds-Studio/Leaf/releases) и используйте его jar вместо `paper.jar`. Остальные шаги те же.

## Что понадобится

- **JDK 25.** Minecraft 26.x и модуль `plugin` (`maven.compiler.release` = 25) требуют Java 25. Подойдёт любая сборка OpenJDK 25, например Eclipse Temurin.
- **Maven 3.9+** (из PATH или встроенный в IntelliJ IDEA).
- **Клиент Minecraft Java Edition** той же версии (26.3), чтобы зайти на сервер.

Проверить версию Java:

```powershell
java -version
where.exe java
```

Если `java -version` показывает не 25, а JDK 25 установлен, найдите его `java.exe`. Обычно он лежит в `C:\Program Files\Eclipse Adoptium\jdk-25...\bin\` или `%USERPROFILE%\.jdks\...\bin\`, если JDK скачан через IntelliJ IDEA. Полный путь к нему пропишите в `start.bat` (см. ниже).

## Структура папок

Папка сервера создаётся **вне репозитория**. Файлы сервера (jar, мир, логи) в Git попадать не должны.

```
<папка-сервера>\
├── paper.jar            ← сервер Paper 26.3
├── start.bat            ← запуск
├── update-plugin.bat    ← обновление плагина
├── eula.txt             ← создаётся при первом запуске
├── server.properties    ← создаётся при первом запуске
├── plugins\
│   └── freedom-plugin.jar
├── logs\
│   └── latest.log
└── world\ ...           ← мир, создаётся автоматически
```

Не кладите папку сервера в каталог, который синхронизируется с облаком (OneDrive, Яндекс Диск и т. п.). Мир и логи постоянно меняются, а клиент синхронизации может блокировать файлы, пока сервер работает.

## 1. Скачать Paper 26.3

Скачивайте Paper только с официального сайта PaperMC: <https://papermc.io/downloads/paper>. Выберите версию **26.3** и последнюю сборку из канала **STABLE** (не beta и не alpha). Сохраните файл в `<папка-сервера>\paper.jar`.

Скачать можно и через официальный API PaperMC (Fill v3). Он сам выдаёт ссылку и контрольную сумму SHA-256:

```powershell
cd <папка-сервера>
$h = @{ 'User-Agent' = 'freedom-local-test-server' }
$resp = Invoke-RestMethod "https://fill.papermc.io/v3/projects/paper/versions/26.3/builds" -Headers $h
$build = @(foreach ($b in $resp) { $b }) | Where-Object channel -eq 'STABLE' |
         Sort-Object { [int]$_.id } -Descending | Select-Object -First 1
$dl = $build.downloads.'server:default'
"Paper 26.3 build $($build.id): $($dl.url)"
Invoke-WebRequest $dl.url -Headers $h -OutFile paper.jar -UseBasicParsing
if ((Get-FileHash paper.jar -Algorithm SHA256).Hash -ieq $dl.checksums.sha256) { 'SHA-256 OK' } else { 'SHA-256 НЕ СОВПАЛА — удалите файл' }
```

## 2. start.bat

```bat
@echo off
chcp 65001 >nul
cd /d "%~dp0"

rem Path to java.exe from JDK 25. If Java 25 is in PATH, "java" is enough.
set "JAVA=java"

"%JAVA%" -Xms1G -Xmx2G -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -jar paper.jar --nogui

pause
```

- `chcp 65001` и флаги `-Dfile.encoding`, `-Dstdout.encoding` и `-Dstderr.encoding` нужны, чтобы русский текст в консоли не превращался в «кракозябры».
- Тестовому серверу хватает `-Xms1G -Xmx2G`.
- `pause` в конце не даёт окну закрыться, если сервер упал с ошибкой.
- Если Java 25 не в PATH, укажите полный путь: `set "JAVA=C:\Program Files\Eclipse Adoptium\jdk-25...\bin\java.exe"`.

Сам `.bat` лучше сохранять в ASCII (без русских букв): так `cmd` не спотыкается о кодировку.

## 3. EULA

Первый запуск (`start.bat`) создаст `eula.txt` и остановится. Прочитайте лицензионное соглашение Mojang: <https://aka.ms/MinecraftEULA>. Если вы с ним согласны, поменяйте строку в `eula.txt` на:

```
eula=true
```

Принимает EULA владелец компьютера, на котором запускается сервер.

## 4. server.properties

После первого запуска в `server.properties` поменяйте:

```properties
server-ip=127.0.0.1
online-mode=true
motd=Freedom test server
max-players=5
```

Остальные параметры оставьте по умолчанию.

- `server-ip=127.0.0.1`: сервер принимает подключения только с этого компьютера. В клиенте подключайтесь к `localhost` или `127.0.0.1`.
- `online-mode`: `true`, если у вас лицензионный клиент. Значение `false` (вход без проверки аккаунта) допустимо **только** при `server-ip=127.0.0.1`. Сервер с `online-mode=false` нельзя открывать в сеть.

## 5. Собрать плагин и положить его на сервер

Из корня `<репозиторий>`:

```powershell
cd <репозиторий>
mvn -pl plugin -am package  # плагин и модули, от которых он зависит
# или: mvn package          # весь проект (бот + плагин)
```

`-am` (also make) собирает вместе с плагином модули, от которых он зависит: позже в плагин будет вшит модуль `common`.

Без Maven в PATH соберите проект в IntelliJ IDEA: окно **Maven** → **Freedom** (корневой проект) → **Lifecycle** → **package**. Maven в IDEA должен работать на JDK 25: **Settings → Build, Execution, Deployment → Build Tools → Maven → Runner → JRE**. Иначе сборка упадёт с `release version 25 not supported`.

Результат сборки: `<репозиторий>\plugin\target\freedom-plugin.jar`. Скопируйте его в `<папка-сервера>\plugins\`:

```powershell
Copy-Item <репозиторий>\plugin\target\freedom-plugin.jar <папка-сервера>\plugins\ -Force
```

## 6. Проверка

Запустите `start.bat` и дождитесь строки `Done (...)! For help, type "help"`.

В консоли или в `logs\latest.log` должны быть строки:

```
[FreedomBot] Enabling FreedomBot v0.1.0-SNAPSHOT
[FreedomBot] Плагин включен, версия 0.1.0-SNAPSHOT
```

Введите в консоли сервера `plugins`: `FreedomBot` должен быть в списке и подсвечен **зелёным**. Красный цвет значит, что плагин не включился.

Остановите сервер командой `stop` и проверьте, что в логе появилась строка:

```
[FreedomBot] Плагин выключен
```

Быстрый поиск по логу:

```powershell
Select-String -Path <папка-сервера>\logs\latest.log -Pattern 'FreedomBot' -Encoding UTF8
```

В логе не должно быть ошибок, связанных с `FreedomBot`: `Could not load plugin`, `Unsupported API version`, `ClassNotFoundException`, `NoClassDefFoundError` и т. п.

## 7. Обновление плагина

1. Остановите сервер командой `stop`. `/reload` не используйте: Paper его не поддерживает как штатный способ обновления, плагины после него ведут себя непредсказуемо.
2. Пересоберите плагин (`mvn -pl plugin -am package`).
3. Скопируйте новый jar в `plugins\` с заменой. Удобно держать для этого в папке сервера `update-plugin.bat`:

```bat
@echo off
chcp 65001 >nul
cd /d "%~dp0"

set "REPO=<репозиторий>"
set "SRC=%REPO%\plugin\target\freedom-plugin.jar"

rem Server must be stopped: nobody listens on 127.0.0.1:<server-port>
set "PORT=25565"
if exist "server.properties" (
    for /f "tokens=2 delims==" %%P in ('findstr /b /c:"server-port=" "server.properties"') do set "PORT=%%P"
)
netstat -ano -p TCP | findstr /r /c:"TCP  *127\.0\.0\.1:%PORT%  *0\.0\.0\.0:0 " >nul && (
    echo [ERROR] The server is running. Type "stop" in the server console first.
    pause
    exit /b 1
)

if not exist "%SRC%" (
    echo [ERROR] Not found: %SRC%
    pause
    exit /b 1
)

if not exist "plugins" mkdir "plugins"
copy /Y "%SRC%" "plugins\freedom-plugin.jar" >nul
for %%F in ("plugins\freedom-plugin.jar") do echo [OK] updated: %%~zF bytes, %%~tF
pause
```

4. Снова запустите `start.bat`.

## Частые ошибки

| Симптом | Причина и решение |
|---|---|
| `UnsupportedClassVersionError ... class file version 69.0` | Сервер или плагин запущен на Java ниже 25. Укажите в `start.bat` полный путь к `java.exe` из JDK 25. |
| `release version 25 not supported` при сборке | Maven работает на старой JDK. Задайте `JAVA_HOME` на JDK 25 (в IDEA: **Settings → Build Tools → Maven → Runner → JRE**). |
| `Could not save spigot.yml` (или другого файла): `Процесс не может получить доступ к файлу, так как этот файл занят другим процессом` | Папка сервера лежит в каталоге, который синхронизируется с облаком (например, рабочий стол в OneDrive), и клиент синхронизации держит файл. Перенесите папку сервера в несинхронизируемое место, например в корень системного диска. |
| Сервер сразу останавливается: `You need to agree to the EULA` | В `eula.txt` всё ещё `eula=false` (см. раздел 3). |
| `Unsupported API version 26.3` | Версия сервера ниже, чем `api-version` в `plugin.yml`. Нужен Paper 26.3. |
| `Could not load 'plugins\freedom-plugin.jar'` / `Invalid plugin.yml` | jar собран неправильно или в `plugin.yml` ошибка. Пересоберите плагин и проверьте `name`, `main` и `api-version`. |
| `ClassNotFoundException: ru.freedomminecraft.plugin.FreedomPlugin` | Поле `main` в `plugin.yml` не совпадает с классом, или класс не попал в jar. |
| В консоли «кракозябры» вместо русского | Нет `chcp 65001` или флагов `-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8` в `start.bat`. |
| `**** FAILED TO BIND TO PORT!` | Порт 25565 занят: например, уже запущена вторая копия сервера. Закройте её или поменяйте `server-port`. |
| Клиент пишет `Failed to verify username` | Клиент без лицензии при `online-mode=true`. Для локального теста поставьте `online-mode=false` (только вместе с `server-ip=127.0.0.1`). |
| Клиент пишет `Outdated server` / `Outdated client` | Версии клиента и сервера не совпадают. Выберите в лаунчере 26.3. |
| Изменения в плагине не видны | Новый jar не скопирован или сервер не перезапущен. Сравните время изменения файла в `plugins\` и в `plugin\target\`. |
