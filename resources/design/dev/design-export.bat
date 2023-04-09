@echo off

setlocal ENABLEDELAYEDEXPANSION
for %%a in (%APP_MODEL_KEYS%) do (
  for /f %%i in ('curl --write-out %%{http_code} --silent --output NUL --user admin:test "%DESIGN_URL%/api/editor/app-models/key/%%a/export"') do set status_code=%%i
  echo.
  if !status_code! == 200 (
    echo.
    echo Downloading app with key = %%a into folder %DESIGN_FOLDER%
    curl --user admin:test "%DESIGN_URL%/api/editor/app-models/key/%%a/export" --output %DESIGN_FOLDER%\%%a%DESIGN_SUFFIX%.zip --fail-with-body
    if errorlevel 22 (
       exit /b %errorlevel%
    )
    echo Downloading app with key = %%a into folder %CUSTOM_FOLDER%
    curl --user admin:test "%DESIGN_URL%/api/editor/app-models/key/%%a/export?type=runtime" --output %CUSTOM_FOLDER%\%%a%CUSTOM_SUFFIX%.zip --fail-with-body
    if errorlevel 22 (
       exit /b %errorlevel%
    )
  ) else (
    echo.
    echo WARNING: There is no app with key = %%a
    exit /b -1
  )
)
