
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractXslStyle {

    public abstract XSSFCellStyle getSheetTitleStyle();

    public abstract XSSFCellStyle getTableTitleStyle();

    public abstract XSSFCellStyle getDescriptionStyle();

    public abstract XSSFCellStyle getDetailTitleStyle();

    public abstract XSSFCellStyle getDetailStyle();

    public abstract XSSFCellStyle getCustomStyle(XSSFWorkbook workbook, int size, XSSFColor fontColor, XSSFColor backColor);

    public abstract void populate(XSSFWorkbook workbook) throws Exception;

    protected final XSSFCellStyle createStyle(XSSFWorkbook workbook, XSSFColor fontColor, XSSFColor backGroundColor, boolean isBold, int fontSize) {
        XSSFCellStyle style = workbook.createCellStyle();

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);

        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillBackgroundColor(backGroundColor);
        style.setFillForegroundColor(backGroundColor);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        style.setWrapText(true);

        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) fontSize);
        font.setFontName("Calibri");
        font.setColor(fontColor);
        style.setFont(font);

        if (isBold) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }

        return style;

    }

}
