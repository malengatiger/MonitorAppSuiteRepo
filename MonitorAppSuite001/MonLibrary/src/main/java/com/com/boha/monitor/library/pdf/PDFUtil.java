package com.com.boha.monitor.library.pdf;

public class PDFUtil {
//
//    public static PdfPCell setHeaderCellStyle(PdfPCell cell) {
//        cell.setPadding(10.0f);
//        cell.setBorderWidth(1.5f);
//        cell.setGrayFill(0.90f);
//        return cell;
//    }
//
//    public static Phrase setHeaderStyle(Phrase p) {
//        p.getFont().setColor(new BaseColor(0x000000));
//        p.getFont().setStyle(Font.BOLD);
//        p.getFont().setSize(Float.parseFloat("16.0"));
//
//        return p;
//    }
//
//    public static Phrase setHeadlineStyle(Phrase p) {
//        p.getFont().setColor(new BaseColor(0x000000));
//        p.getFont().setStyle(Font.BOLD);
//        p.getFont().setSize(Float.parseFloat("22.0"));
//
//        return p;
//    }
//
//    public static Phrase setBoldBlue(Phrase p) {
//        p.getFont().setColor(new BaseColor(0x0000ff));
//        p.getFont().setStyle(Font.BOLD);
//        p.getFont().setSize(Float.parseFloat("14.0"));
//        return p;
//    }
//
//    public static Phrase setBoldBlack(Phrase p) {
//        p.getFont().setColor(new BaseColor(0x000000));
//        p.getFont().setStyle(Font.BOLD);
//        p.getFont().setSize(Float.parseFloat("14.0"));
//        return p;
//    }
//
//    public static Phrase setBoldRed(Phrase p) {
//        p.getFont().setColor(new BaseColor(0xff0000));
//        p.getFont().setStyle(Font.BOLD);
//        p.getFont().setSize(Float.parseFloat("14.0"));
//        return p;
//    }
//
//    public static Phrase setSetNormalBlack(Phrase p) {
//        p.getFont().setColor(new BaseColor(0x000000));
//        p.getFont().setStyle(Font.NORMAL);
//        p.getFont().setSize(Float.parseFloat("14.0"));
//        return p;
//    }
//
//    public static void addLogo(Context ctx, Document doc)
//            throws DocumentException {
//
//        try {
//            Drawable d = ctx.getResources().getDrawable(R.drawable.xred_oval);
//            Bitmap bitmap = ((BitmapDrawable) d).drawTextToBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] bitmapdata = stream.toByteArray();
//
//            Image image1 = Image.getInstance(bitmapdata);
//            image1.setAlignment(Image.ALIGN_LEFT);
//            doc.add(image1);
//
//        } catch (BadElementException ex) {
//            throw new DocumentException("Bad Element Exception");
//        } catch (IOException ex) {
//            throw new DocumentException("IOException");
//        }
//    }
//
//    public static PdfPTable getTable(int columns, int[] widths) throws DocumentException {
//        PdfPTable table = new PdfPTable(columns);
//        table.setWidths(widths);
//        table.setSpacingBefore(5.0f);
//        table.getDefaultCell().setPadding(5.0f);
//        table.setSpacingAfter(10.0f);
//
//        return table;
//    }
//
//    public static Paragraph getHeaderTitle22(String title) throws DocumentException {
//        float size = Float.parseFloat("22.0");
//        Paragraph par = new Paragraph();
//        par.setSpacingBefore(5);
//        par.setFont(getHelveticaFont());
//        par.setAlignment(Paragraph.ALIGN_CENTER);
//        par.getFont().setStyle(Font.BOLD);
//        par.getFont().setSize(size);
//        par.setSpacingAfter(10);
//        par.add(title);
//        return par;
//    }
//
//    public static Paragraph getHeaderTitle18(String title) throws DocumentException {
//        float size = Float.parseFloat("18.0");
//        Paragraph par = new Paragraph();
//        par.setSpacingBefore(5);
//        par.setFont(getHelveticaFont());
//        par.setAlignment(Paragraph.ALIGN_CENTER);
//        par.getFont().setStyle(Font.BOLD);
//        par.getFont().setSize(size);
//        par.setSpacingAfter(5);
//        par.add(title);
//        return par;
//    }
//
//    public static Paragraph getHeaderTitle16(String title) throws DocumentException {
//        float size = Float.parseFloat("18.6");
//        Paragraph par = new Paragraph();
//        par.setSpacingBefore(5);
//        par.setFont(getHelveticaFont());
//        par.setAlignment(Paragraph.ALIGN_CENTER);
//        par.getFont().setStyle(Font.BOLD);
//        par.getFont().setSize(size);
//        par.setSpacingAfter(5);
//        par.add(title);
//        return par;
//    }
//
//    static BaseFont bf_helv;
//    static BaseFont bf_times;
//    static BaseFont bf_courier;
//    static BaseFont bf_symbol;
//    static HashMap<String, String> imgMap;
//
//    public static Font getHelveticaFont() throws DocumentException {
//        setFonts();
//        return new Font(bf_helv);
//    }
//
//    public static Font getTimesFont() throws DocumentException {
//        setFonts();
//        return new Font(bf_times);
//    }
//
//    public static Font getSymbolFont() throws DocumentException {
//        setFonts();
//        return new Font(bf_symbol);
//    }
//
//    public static Font getCourierFont() throws DocumentException {
//        setFonts();
//        return new Font(bf_courier);
//    }
//
//    private static void setFonts() throws DocumentException {
//        if (bf_helv == null) {
//            try {
//                bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252",
//                        false);
//                bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252",
//                        false);
//                bf_courier = BaseFont.createFont(BaseFont.COURIER, "Cp1252",
//                        false);
//                bf_symbol = BaseFont.createFont(BaseFont.SYMBOL, "Cp1252",
//                        false);
//            } catch (IOException ex) {
//                throw new DocumentException();
//            }
//        }
//
//    }

}
