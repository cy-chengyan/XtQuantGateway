
"C:\XtOrderPlacer\nssm.exe" stop XtOrderPlacer

timeout /t 5 > nul

"C:\XtOrderPlacer\nssm.exe" remove XtOrderPlacer confirm


"C:\XtOrderPlacer\nssm.exe" install XtOrderPlacer "C:\XtOrderPlacer\OrderPlacer.bat"
"C:\XtOrderPlacer\nssm.exe" set XtOrderPlacer Start SERVICE_DEMAND_START
"C:\XtOrderPlacer\nssm.exe" set XtOrderPlacer DisplayName "XtOrderPlacer"
"C:\XtOrderPlacer\nssm.exe" set XtOrderPlacer Description "Order placer for XtQuant"
"C:\XtOrderPlacer\nssm.exe" start XtOrderPlacer
