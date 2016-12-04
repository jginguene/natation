
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractXlsMaker {

    protected XSSFWorkbook workbook;
    protected AbstractXslStyle style;

    private String fileName;

    public AbstractXlsMaker() {

    }

    public final void makeReport(String fileName, AbstractXslStyle style) throws Exception {
        this.fileName = fileName;
        this.style = style;
        this.createFWorkbook(this.fileName);
        style.populate(this.workbook);

        this.populateWorkbook();

        this.writeFile();

        System.out.println("File " + fileName + " created");
    }

    protected abstract void populateWorkbook() throws Exception;

    private final void createFWorkbook(String fileName) throws Exception {
        try {
            File file = new File(fileName);
            file.delete();
            file.createNewFile();
            this.workbook = new XSSFWorkbook();
        } catch (Exception e) {
            throw new Exception("createFile " + fileName + " failed", e);
        }
    }

    private final void writeFile() throws Exception {
        try {

            FileOutputStream fileOut = new FileOutputStream(this.fileName);
            this.workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            throw new Exception("writeFile " + this.fileName + " failed");
        }

    }

    protected final void setCellFormula(XSSFSheet sheet, int x, int y, String formula, XSSFCellStyle style) {
        XSSFRow row = sheet.getRow(x);
        if (row == null) {
            row = sheet.createRow(x);
        }

        XSSFCell cell = row.createCell(y);
        cell.setCellFormula(formula);
        cell.setCellStyle(style);
    }

    protected final void setCellValue(XSSFSheet sheet, int x, int y, String value, XSSFCellStyle style) {
        XSSFRow row = sheet.getRow(x);
        if (row == null) {
            row = sheet.createRow(x);
        }

        XSSFCell cell = row.createCell(y);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    protected final void setCellValue(XSSFSheet sheet, int x, int y, int value, XSSFCellStyle style) {
        XSSFRow row = sheet.getRow(x);
        if (row == null) {
            row = sheet.createRow(x);
        }

        XSSFCell cell = row.createCell(y);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    protected final void setCellValue(XSSFSheet sheet, int x, int y, double value, XSSFCellStyle style) {
        XSSFRow row = sheet.getRow(x);
        if (row == null) {
            row = sheet.createRow(x);
        }

        XSSFCell cell = row.createCell(y);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    protected final void setCellValue(XSSFSheet sheet, int x, int y, Date value, XSSFCellStyle style) {
        XSSFRow row = sheet.getRow(x);
        if (row == null) {
            row = sheet.createRow(x);
        }

        XSSFCell cell = row.createCell(y);

        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    protected final char getLetter(int col) {
        return (char) (new Character('A').charValue() + (char) col);
    }

    protected XSSFSheet createSheet(String name, int... columnSize) {
        XSSFSheet sheet = this.workbook.createSheet(name);

        int i = 0;
        for (int size : columnSize) {
            sheet.setColumnWidth(i, size);
            i++;
        }

        return sheet;
    }

}
