
"C:\XtOrderPlacer\nssm.exe" stop XtOrderPlacer

timeout /t 5 > nul

"C:\XtOrderPlacer\nssm.exe" remove XtOrderPlacer confirm
