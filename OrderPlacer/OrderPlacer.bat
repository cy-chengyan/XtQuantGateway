
@set "VIRTUAL_ENV=C:\XtOrderPlacer\.venv"
@set "PATH=%VIRTUAL_ENV%\Scripts;%PATH%"
@set "PYTHONPATH=C:\XtOrderPlacer"

cd /d C:\XtOrderPlacer
C:\XtOrderPlacer\.venv\Scripts\python.exe .\task\OrderPlacer.py %*
