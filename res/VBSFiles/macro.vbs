Dim Excel
Dim ExcelDoc

Set Excel = CreateObject("Excel.Application")

'Open the Document
Set ExcelDoc = Excel.Workbooks.open("C:\Users\user\Documents\Risorse_Software\FaxMailProjectATHome\Cartella Rapporti Excel\ITRON ITALIA S.P.A. 3 ott 2020, 05 36 56.xlsx")
Excel.ActiveSheet.ExportAsFixedFormat 0, "C:\Users\user\Music\Cartella Rapporti PDF\ITRON ITALIA S.P.A. 3 ott 2020, 05 36 56.pdf" ,0, 1, 0,,,0
Excel.ActiveWorkbook.Close
Excel.Application.Quit
WScript.quit 1