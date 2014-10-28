package com.com.boha.monitor.library.pdf;

public class InvoicePDF {

//
//    static final String LOG = InvoicePDF.class.getSimpleName();
//	public static File getInvoicePDF(Context ctx, InvoiceDTO inv) throws DocumentException {
//		Document document = new Document(PageSize.A4, 10, 10, 10, 10);
//		File dir = ctx.getFilesDir();
//		// create company directory
//		File classDir = new File(dir, inv.getCompanyClient().getCompany().getCompanyName());
//		if (!classDir.exists()) {
//			classDir.mkdir();
//		}
//		File file = new File(classDir, inv.getCompanyClient().getClient().getClientName()
//				 + ".pdf");
//
//		if (file.exists()) {
//			file.delete();
//		}
//
//		try {
//			PdfWriter writer = PdfWriter.getInstance(document,
//					new FileOutputStream(file));
//			writer.setBoxSize("art", new Rectangle(10, 10, 559, 788));
//			document.open();
//			PDFUtil.addLogo(ctx, document);
//			//addPeriodHeader(document, className, startDate, endDate, list);
//			//addPeriodBody(document, list);
//			document.add(new Phrase("*** End of Period Attendance Report ***"));
//			document.close();
//		} catch (BadElementException ex) {
//			Log.e(LOG, "Bad element ", ex);
//			throw new DocumentException();
//		} catch (FileNotFoundException ex) {
//			Log.e(LOG, "Report file not found");
//			throw new DocumentException();
//		}
//
//		return file;
//	}

//	public static void addPeriodHeader(Document doc, String className,
//			long startDate, long endDate, List<AttendanceAggregate> list)
//			throws DocumentException {
//
//
//		doc.add(PDFUtil.getHeaderTitle18("Period Attendance Report"));
//		doc.add(PDFUtil.getHeaderTitle16(className));
//
//		StringBuilder sb = new StringBuilder();
//		sb.append(Util.getLongerDate(new Date(startDate))).append(" to ");
//		sb.append(Util.getLongerDate(new Date(endDate)));
//
//		doc.add(PDFUtil.getHeaderTitle16(sb.toString()));
//		// add table with totals...
//		int[] widths = {160,60};
//		PdfPTable table = PDFUtil.getTable(2, widths);
//
//		Totals tot = getTotals(list);
//
//		PdfPCell cell0 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("Days in Period"))));
//		PdfPCell cell0a = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("" + list.get(0).getTotalDays()))));
//
//		PdfPCell cell1 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("Percentage Absent"))));
//		Phrase pp = new Phrase(Util.getTruncated(tot.getPercAbsent().doubleValue()) + " %");
//		setAbsenceStyle(pp);
//
//		PdfPCell cell1a = setHeaderCellStyle(new PdfPCell(pp));
//
//		PdfPCell cell2 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("Average Students Absent per Day"))));
//		Phrase pa = new Phrase(Util.getTruncated(tot.getAverageAbsence().doubleValue()));
//		setAbsenceStyle(pa);
//		PdfPCell cell2a = setHeaderCellStyle(new PdfPCell(pa));
//		table.addCell(cell0);
//		table.addCell(cell0a);
//		table.addCell(cell1);
//		table.addCell(cell1a);
//		table.addCell(cell2);
//		table.addCell(cell2a);
//
//		PdfPCell cell3 = new PdfPCell();
//		cell3.setColspan(2);
//		cell3.setGrayFill(0.95f);
//		table.addCell(cell3);
//		table.addCell(cell3);
//
//		PdfPCell cell4 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("Percentage Present"))));
//		PdfPCell cell4a = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase(Util.getTruncated(tot.getPercPres().doubleValue()) + " %"))));
//		PdfPCell cell5 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("Average Students Present per Day"))));
//		PdfPCell cell5a = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase(Util.getTruncated(tot.getAvgPresence().doubleValue())))));
//		table.addCell(cell4);
//		table.addCell(cell4a);
//		table.addCell(cell5);
//		table.addCell(cell5a);
//
//		doc.add(table);
//		// set up first row
//
//	}
//
//	private static Totals getTotals(List<AttendanceAggregate> list) {
//		Totals tot = new Totals();
//		int totAbs = 0, totPres = 0;
//		for (AttendanceAggregate a : list) {
//			totAbs += a.getTotalAbsent();
//			totPres += a.getTotalPresent();
//		}
//		BigDecimal total = new BigDecimal(totAbs + totPres);
//		BigDecimal absent = new BigDecimal(totAbs);
//		BigDecimal pres = new BigDecimal(totPres);
//		BigDecimal percentAbsent = new BigDecimal(0);
//		BigDecimal percentPresent = new BigDecimal(0);
//		if (total.intValue() > 0) {
//			percentAbsent = absent.divide(total, 2,
//					BigDecimal.ROUND_UP);
//			double dd = percentAbsent.doubleValue();
//			dd = dd * 100;
//			percentAbsent = new BigDecimal(dd);
//			percentPresent = pres.divide(total, 2,
//					BigDecimal.ROUND_DOWN);
//			dd = percentPresent.doubleValue();
//			dd = dd * 100;
//			percentPresent = new BigDecimal(dd);
//		}
//
//
//		if (list == null || list.size() == 0) {
//			return tot;
//		}
//		int days = list.get(0).getTotalDays();
//		BigDecimal avgAbsent = absent.divide(new BigDecimal(days), 1,
//				BigDecimal.ROUND_UP);
//		BigDecimal avgPresent = pres.divide(new BigDecimal(days), 1,
//				BigDecimal.ROUND_DOWN);
//		tot.setAverageAbsence(avgAbsent);
//		tot.setAvgPresence(avgPresent);
//		tot.setPercAbsent(percentAbsent);
//		tot.setPercPres(percentPresent);
//		tot.setTotalAbsent(totAbs);
//		tot.setTotalPresent(totPres);
//
//		return tot;
//	}
//
//	public static void addPeriodBody(Document doc,
//			List<AttendanceAggregate> list) throws DocumentException {
//
//		int[] widths = { 150, 60, 60, 100, 100 };
//		PdfPTable table = PDFUtil.getTable(5, widths);
//		table.setHeaderRows(1);
//
//		// set header captions - name, totalabs, totpres, avgAbs, avgPres
//		PdfPCell cell1 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("Student Name"))));
//		PdfPCell cell2 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("Abs"))));
//		PdfPCell cell3 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("Pres"))));
//		PdfPCell cell4 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("% Abs"))));
//		PdfPCell cell5 = setHeaderCellStyle(new PdfPCell(
//				setHeaderStyle(new Phrase("% Pres"))));
//
//		table.addCell(cell1);
//		table.addCell(cell2);
//		table.addCell(cell3);
//		table.addCell(cell4);
//		table.addCell(cell5);
//		//
//
//		//
//		boolean isAltColor = false;
//		for (AttendanceAggregate aggr : list) {
//			addPeriodRow(table, aggr, isAltColor);
//			isAltColor = !isAltColor;
//		}
//		doc.add(table);
//	}
//
//	public static void addPeriodRow(PdfPTable table, AttendanceAggregate aggr,
//			boolean isAltColor) throws DocumentException {
//
//		StringBuilder sb = new StringBuilder();
//		sb.append(aggr.getFirstName()).append(" ").append(aggr.getLastName());
//		PdfPCell cell1 = new PdfPCell(new Phrase(sb.toString()));
//		cell1.setPadding(5.0f);
//		Phrase p1 = null;
//		if (aggr.getTotalAbsent() > 0) {
//			p1 = new Phrase("" + aggr.getTotalAbsent());
//		} else {
//			p1 = new Phrase("");
//		}
//		setAbsenceStyle(p1);
//		PdfPCell cell2 = new PdfPCell(p1);
//		cell2.setPadding(5.0f);
//		cell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		Phrase p3 = null;
//		if (aggr.getTotalPresent() > 0) {
//			p3 = new Phrase("" + aggr.getTotalPresent());
//		} else {
//			p3 = new Phrase("");
//		}
//		setPresentStyle(p3);
//
//		PdfPCell cell3 = new PdfPCell(p3);
//		cell3.setPadding(5.0f);
//		cell3.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		//
//		BigDecimal total = new BigDecimal(aggr.getTotalAbsent()
//				+ aggr.getTotalPresent());
//		BigDecimal absent = new BigDecimal(aggr.getTotalAbsent());
//		BigDecimal pres = new BigDecimal(aggr.getTotalPresent());
//
//		BigDecimal percentAbsent = absent.divide(total, 2,
//				BigDecimal.ROUND_HALF_EVEN);
//		double dd = percentAbsent.doubleValue();
//		dd = dd * 100;
//		percentAbsent = new BigDecimal(dd, new MathContext(3));
//		//
//		BigDecimal percentPresent = pres.divide(total, 2,
//				BigDecimal.ROUND_HALF_EVEN);
//		dd = percentPresent.doubleValue();
//		dd = dd * 100;
//		percentPresent = new BigDecimal(dd, new MathContext(4));
//		//
//		Phrase p4 = null;
//		if (aggr.getTotalAbsent() > 0) {
//			p4 = new Phrase(Util.getTruncated(percentAbsent.doubleValue()) + " %");
//		} else {
//			p4 = new Phrase("");
//		}
//		setAbsenceStyle(p4);
//		PdfPCell cell4 = new PdfPCell(p4);
//		cell4.setPadding(5.0f);
//		cell4.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		Phrase p5 = null;
//
//		if (aggr.getTotalPresent() > 0) {
//			p5 = new Phrase(Util.getTruncated(percentPresent.doubleValue()) + " %");
//			if (aggr.getTotalAbsent() == 0) {
//				setPerfectStyle(p5);
//			} else {
//				setPresentStyle(p5);
//			}
//
//		} else {
//			p5 = new Phrase("");
//			setPresentStyle(p5);
//		}
//
//		PdfPCell cell5 = new PdfPCell(p5);
//		cell5.setPadding(5.0f);
//		cell5.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		//
//		if (isAltColor) {
//			cell1.setGrayFill(0.95f);
//			cell2.setGrayFill(0.95f);
//			cell3.setGrayFill(0.95f);
//			cell4.setGrayFill(0.95f);
//			cell5.setGrayFill(0.95f);
//		}
//		table.addCell(cell1);
//		table.addCell(cell2);
//		table.addCell(cell3);
//		table.addCell(cell4);
//		table.addCell(cell5);
//
//		//
//	}
//
//	public static File getDailyAttendancePDF(Context ctx, String teacherName,
//			String className, long date, List<AttendanceRecord> list)
//			throws DocumentException {
//		Document document = new Document(PageSize.A4, 10, 10, 10, 10);
//		File dir = Util.getAttendanceDirectory();
//		// create class directory
//		File classDir = new File(dir, className);
//		if (!classDir.exists()) {
//			classDir.mkdir();
//		}
//		File file = new File(classDir, className + " - "
//				+ Util.getLongDateForPDF(date) + ".pdf");
//
//		if (file.exists()) {
//			file.delete();
//		}
//
//		try {
//			PdfWriter writer = PdfWriter.getInstance(document,
//					new FileOutputStream(file));
//			writer.setBoxSize("art", new Rectangle(10, 10, 559, 788));
//
//			document.open();
//			PDFUtil.addLogo(ctx, document);
//			addHeader(ctx, document, className, date);
//			addSummary(document, list);
//			addTable(ctx, document, list);
//			document.add(new Phrase("\n*** End of Attendance Report ***"));
//			document.close();
//
//		} catch (BadElementException ex) {
//			throw new DocumentException();
//		} catch (FileNotFoundException ex) {
//			Log.e("PDFGen", "Report file not found");
//			throw new DocumentException();
//		}
//		return file;
//	}
//
//	private static void addTable(Context ctx, Document doc,
//			List<AttendanceRecord> list) throws DocumentException {
//		int[] widths = { 80, 400 };
//		PdfPTable table = PDFUtil.getTable(2, widths);
//		table.setHeaderRows(1);
//
//		float size = Float.parseFloat("14.0");
//		String name = "Class Attendance Status List";
//
//		Phrase pName = new Phrase(name);
//		pName.getFont().setSize(size);
//		pName.getFont().setStyle(Font.BOLD);
//		PdfPCell cell = new PdfPCell(pName);
//		cell.setPadding(5);
//		cell.setColspan(2);
//		cell.setBorderColor(new BaseColor(000000));
//		table.addCell(cell);
//		boolean isAltColor = false;
//		for (AttendanceRecord rec : list) {
//			addRow(table, rec, isAltColor);
//			isAltColor = !isAltColor;
//		}
//
//		doc.add(table);
//	}
//
//	private static void addSummary(Document doc, List<AttendanceRecord> list)
//			throws DocumentException {
//		PdfPTable table = new PdfPTable(2);
//		int[] widths = { 100, 40 };
//		table.setWidths(widths);
//		table.setTotalWidth(160.0f);
//		table.setSpacingBefore(5);
//		table.getDefaultCell().setPadding(5);
//		table.setSpacingAfter(10.0f);
//		//
//		int pCount = 0, aCount = 0;
//		for (AttendanceRecord rec : list) {
//			if (rec.getStatus() == AttendanceRecord.ABSENT) {
//				aCount++;
//			} else {
//				pCount++;
//			}
//		}
//
//		Phrase pName = setHeaderStyle(new Phrase("Total Students Present"));
//		setHeaderStyle(pName);
//
//		PdfPCell cell = new PdfPCell(pName);
//		setHeaderCellStyle(cell);
//		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		table.addCell(cell);
//		//
//
//		Phrase pPresent = new Phrase("" + pCount);
//		setHeaderStyle(pPresent);
//		pPresent.getFont().setColor(new BaseColor(0x0000ff));
//		PdfPCell cell2 = new PdfPCell(pPresent);
//		setHeaderCellStyle(cell2);
//		cell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		table.addCell(cell2);
//		//
//		Phrase pAbs = setHeaderStyle(new Phrase("Total Students Absent"));
//		setHeaderStyle(pAbs);
//		PdfPCell cell4 = new PdfPCell(pAbs);
//		setHeaderCellStyle(cell4);
//		cell4.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		table.addCell(cell4);
//		//
//
//		Phrase pAbsCnt = new Phrase("" + aCount);
//		setAbsenceStyle(pAbsCnt);
//		PdfPCell cell3 = new PdfPCell(pAbsCnt);
//		setHeaderCellStyle(cell3);
//		cell3.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		table.addCell(cell3);
//
//		// calc perc
//		BigDecimal total = new BigDecimal(aCount + pCount);
//		BigDecimal absent = new BigDecimal(aCount);
//
//		BigDecimal percentAbsent = absent.divide(total, 3,
//				BigDecimal.ROUND_HALF_EVEN);
//		double dd = percentAbsent.doubleValue();
//		dd = dd * 100;
//		percentAbsent = new BigDecimal(dd, new MathContext(3));
//		PdfPCell percCell = new PdfPCell(setHeaderStyle(new Phrase(
//				"Percentage Students Absent")));
//		setHeaderCellStyle(percCell);
//		percCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		//
//		PdfPCell perc = new PdfPCell(setHeaderStyle(new Phrase(""
//				+ Util.getTruncated(percentAbsent.doubleValue()) + " %")));
//		setHeaderCellStyle(perc);
//		perc.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		table.addCell(percCell);
//		table.addCell(perc);
//
//		doc.add(table);
//	}
//
//	private static void addRow(PdfPTable table, AttendanceRecord rec,
//			boolean isAltColor) throws DocumentException {
//		Font font = PDFUtil.getHelveticaFont();
//
//		Phrase pStatus = null;
//		PdfPCell cell = null;
//		if (rec.getStatus() == AttendanceRecord.ABSENT) {
//			pStatus = new Phrase("Absent", font);
//			setAbsenceStyle(pStatus);
//		} else {
//			pStatus = new Phrase("Present", font);
//			setPresentStyle(pStatus);
//		}
//		cell = new PdfPCell(pStatus);
//		cell.setPadding(5.0f);
//		if (isAltColor) {
//			cell.setGrayFill(0.95f);
//		}
//		table.addCell(cell);
//		//
//		Phrase pData = new Phrase(rec.getFirstName() + " " + rec.getLastName());
//		pData.setFont(font);
//		pData.getFont().setSize(Float.parseFloat("14.0"));
//		pData.getFont().setStyle(Font.NORMAL);
//
//		PdfPCell cellData = new PdfPCell(pData);
//		cellData.setPadding(5);
//		cellData.setBorderColor(new BaseColor(333));
//		if (isAltColor) {
//			cellData.setGrayFill(0.95f);
//		}
//
//		table.addCell(cellData);
//	}
//
//	private static void addHeader(Context ctx, Document doc, String className,
//			long date) throws DocumentException {
//
//		//
//		float size = Float.parseFloat("18.0");
//		Paragraph par = new Paragraph();
//		par.setSpacingBefore(5);
//		par.setFont(PDFUtil.getHelveticaFont());
//		par.setAlignment(Paragraph.ALIGN_CENTER);
//		par.getFont().setStyle(Font.BOLD);
//		par.getFont().setSize(size);
//		par.setSpacingAfter(5);
//		par.add("Daily Attendance Report");
//
//		doc.add(par);
//		//
//		// size = Float.parseFloat("14.0");
//		Paragraph par2 = new Paragraph();
//		par2.setSpacingBefore(5);
//		par2.setFont(PDFUtil.getHelveticaFont());
//		par2.setAlignment(Paragraph.ALIGN_LEFT);
//		par2.getFont().setStyle(Font.BOLD);
//		par2.getFont().setSize(size);
//		par2.setSpacingAfter(5);
//		par2.add(className);
//		doc.add(par2);
//		//
//		// size = Float.parseFloat("12.0");
//		Paragraph par3 = new Paragraph();
//		par3.setSpacingBefore(5);
//		par3.setFont(PDFUtil.getHelveticaFont());
//		par3.setAlignment(Paragraph.ALIGN_LEFT);
//		par3.getFont().setStyle(Font.BOLD);
//		par3.getFont().setSize(size);
//		par3.setSpacingAfter(10);
//		par3.add(Util.getLongerDate(new Date(date)));
//		doc.add(par3);
//	}
//
//
//
//	private static PdfPCell setHeaderCellStyle(PdfPCell cell) {
//		cell.setPadding(10.0f);
//		cell.setBorderWidth(1.5f);
//		cell.setGrayFill(0.90f);
//		return cell;
//	}
//
//	private static Phrase setHeaderStyle(Phrase p) {
//		p.getFont().setColor(new BaseColor(0x000000));
//		p.getFont().setStyle(Font.BOLD);
//		p.getFont().setSize(Float.parseFloat("16.0"));
//
//		return p;
//	}
//
//	private static void setAbsenceStyle(Phrase p) {
//		p.getFont().setColor(new BaseColor(0xff0000));
//		p.getFont().setStyle(Font.BOLD);
//		p.getFont().setSize(Float.parseFloat("14.0"));
//	}
//
//	private static void setPerfectStyle(Phrase p) {
//		p.getFont().setColor(new BaseColor(0x0000ff));
//		p.getFont().setStyle(Font.BOLD);
//		p.getFont().setSize(Float.parseFloat("14.0"));
//	}
//
//	private static void setPresentStyle(Phrase p) {
//		p.getFont().setColor(new BaseColor(0x000000));
//		p.getFont().setStyle(Font.NORMAL);
//		p.getFont().setSize(Float.parseFloat("14.0"));
//	}
//
//	static class Totals {
//		private int totalAbsent, totalPresent;
//		private BigDecimal averageAbsence, avgPresence, percAbsent, percPres;
//
//		public int getTotalAbsent() {
//			return totalAbsent;
//		}
//
//		public void setTotalAbsent(int totalAbsent) {
//			this.totalAbsent = totalAbsent;
//		}
//
//		public int getTotalPresent() {
//			return totalPresent;
//		}
//
//		public void setTotalPresent(int totalPresent) {
//			this.totalPresent = totalPresent;
//		}
//
//		public BigDecimal getAverageAbsence() {
//			return averageAbsence;
//		}
//
//		public void setAverageAbsence(BigDecimal averageAbsence) {
//			this.averageAbsence = averageAbsence;
//		}
//
//		public BigDecimal getAvgPresence() {
//			return avgPresence;
//		}
//
//		public void setAvgPresence(BigDecimal avgPresence) {
//			this.avgPresence = avgPresence;
//		}
//
//		public BigDecimal getPercAbsent() {
//			return percAbsent;
//		}
//
//		public void setPercAbsent(BigDecimal percAbsent) {
//			this.percAbsent = percAbsent;
//		}
//
//		public BigDecimal getPercPres() {
//			return percPres;
//		}
//
//		public void setPercPres(BigDecimal percPres) {
//			this.percPres = percPres;
//		}
//	}
}
