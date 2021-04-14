package com.netc.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.winnovature.dao.CheckSession;
import com.winnovature.dao.RegenerateChallanDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/challan/generate")
public class RegenerateChallan extends HttpServlet {
	static Logger log = Logger.getLogger(RegenerateChallan.class.getName());
	private static final long serialVersionUID = 1L;

	//private static String contextPath = "E:/challan/";
	//private static final String fastagLogo = "E:/challan/fastag_suco.png";
	//private static final String fastagLogo = "/home/challan/fastag_suco.png";
	private static final String fastagLogo = "/home/challan/fastag_upcb.png";
	private static String contextPath = "/home/challan/";
	
	//public static final String bankLogo = "E:/challan/sucobank.jpg";
	/*
	private static String contextPath = "/home/challan/";
	public static final String fastagLogo = "/home/challan/fastag.png";
	public static final String bankLogo = "/home/challan/sucobank.jpg";
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("RegenerateChallan.java ::  welcome");
		JSONObject jreq = new JSONObject();

		PrintWriter out = null;//response.getWriter();

		StringBuffer sbuffer = new StringBuffer();
		String line = null;
		JSONObject resp = null;

		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sbuffer.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			jreq = new JSONObject(sbuffer.toString());

			log.info("Json Request For Regenerate Challan : " + jreq.toString());
			// JSONObject jsonreq = jreq.getJSONObject("RGChallan");

			String vehicleNumber = jreq.getString("vehicleNumber");
			// String userId = jreq.getString("userId");
			// RegenerateChallanDAO regenerateChallanDAO = new RegenerateChallanDAO();
			JSONObject challanDetails = RegenerateChallanDAO.getChallanData(vehicleNumber, conn);
			resp = new JSONObject();
			if (challanDetails.getString("vehStatus").equalsIgnoreCase("present")) {

				String pdfFileName = vehicleNumber + "_challan.pdf";
				log.info("Creating Challan file...");
				String path = contextPath + pdfFileName;
				generateChallanPdf(challanDetails.getString("challanId"), challanDetails.getString("tid"),
						challanDetails.getString("tagId"), challanDetails.getString("engineNumber"),
						challanDetails.getString("chassisNumber"), vehicleNumber, challanDetails.getString("createdDate"),
						path, challanDetails.getString("barCode"));
				resp.put("status", "1");
				resp.put("message", "Challan generated successfully.");
				
				
				////////////////////////////////////////////
				String filePath = contextPath+vehicleNumber+"_challan.pdf";
				File downloadFile = new File(filePath);
				FileInputStream inStream = new FileInputStream(downloadFile);
				
				// if you want to use a relative path to context root:
				//String relativePath = getServletContext().getRealPath("");
				//log.info("relativePath = " + relativePath);
				
				// obtains ServletContext
				ServletContext context = getServletContext();
				
				// gets MIME type of the file
				String mimeType = context.getMimeType(filePath);
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				log.info("MIME type : " + mimeType);
				
				// modifies response
				response.setContentType(mimeType);
				response.setContentLength((int) downloadFile.length());
				
				// forces download
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);
				
				// obtains response's output stream
				OutputStream outStream = response.getOutputStream();
				
				byte[] buffer = new byte[4096];
				int bytesRead = -1;
				
				while ((bytesRead = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
				
				inStream.close();
				outStream.close();

			} else {
				out = response.getWriter();
				log.info("Vehicle Not Found...");
				resp.put("status", "0");
				resp.put("message", "Vehicle number not found.");
				
				log.info("Vehicle Number " + vehicleNumber);
				log.info("*****************Response to RegenerateChallan API()****************");
				log.info("--------------------------------------------------------------------------------");
				log.info(resp);
				log.info("--------------------------------------------------------------------------------");
				out.write(resp.toString());
				
			}

			
		} catch (Exception e) {
			log.info("Exception in RegenerateChallan.java");
			e.printStackTrace();
		} finally {
			MemoryComponent.closePrintWriter(out);
			DatabaseManager.commitConnection(conn);
		}

	}

	public void generateChallanPdf(String challanId, String tid, String tagId, String engineNumber,
			String chassisNumber, String vehicleNumber, String createdDate, String challanFilePath,
			String barcodenumber) throws IOException, ParseException {

		DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat targetFormat1 = new SimpleDateFormat("hh:mm:ss a");
		Date date = originalFormat.parse(createdDate);
		String formattedDate = targetFormat.format(date);
		String formattedDate1 = targetFormat1.format(date);

		System.out.println(formattedDate + ":::::::::::" + formattedDate1);

		log.info("generateChallanPdf() Challan File Path :: " + challanFilePath);
		PdfWriter writer = null;
		// Creating a Document
		Document document = null;
		try {
			writer = new PdfWriter(challanFilePath);
			// Creating a PdfDocument
			PdfDocument pdf = new PdfDocument(writer);
			// Creating a Document
			document = new Document(pdf);
			// Creating an ImageData object
			ImageData data = ImageDataFactory.create(fastagLogo);
			Image image = new Image(data);
			
			// Image image1 = new Image(data1);
			//image.setHeight(70);
			//image.setWidth(100);
			image.setHorizontalAlignment(HorizontalAlignment.CENTER);
			// Adding image to the document
			document.add(image);
			///ADDING SECOND IMAGE///
			/*
			ImageData bank = ImageDataFactory.create(bankLogo);
			Image bankImage = new Image(bank);
			bankImage.setHeight(70);
			bankImage.setWidth(100);
			bankImage.setHorizontalAlignment(HorizontalAlignment.RIGHT);
			document.add(bankImage);
			*/
			// image1.setHorizontalAlignment(HorizontalAlignment.RIGHT);
			// image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
			// float x = (PageSize.A4.getWidth() - image.getImageScaledWidth()) / 2;
			// float y = (PageSize.A4.getHeight() - image.getImageScaledHeight()) / 2;
			// image.setFixedPosition(x, y);

			StringBuilder strBuilder = new StringBuilder();

			strBuilder.append("\n");
			strBuilder.append("Proof of fitment of FASTag");
			strBuilder.append("\n");
			Paragraph notePara = new Paragraph(strBuilder.toString());
			notePara.setTextAlignment(TextAlignment.CENTER).setBold().setUnderline();
			notePara.setFontSize(16);
			document.add(notePara);

			StringBuilder strBuilder2 = new StringBuilder();

			strBuilder2.append("Fitment Challan Number : " + challanId);
			strBuilder2.append("\n");
			strBuilder2.append("\n");
			strBuilder2.append("Dated :   " + formattedDate + "             Time:    " + formattedDate1);
			strBuilder2.append("\n");
			strBuilder.append("\n");

			Paragraph notePara1 = new Paragraph(strBuilder2.toString());
			notePara1.setTextAlignment(TextAlignment.LEFT);
			// notePara1.setFontSize(11);
			document.add(notePara1);

			// PdfFont small = PdfFontFactory.createFont(FontConstants.HELVETICA);
			Text asterisk = new Text("*").addStyle(new Style().setFontColor(Color.RED));

			Table table = new Table(new float[] { 180F, 180F }).setHorizontalAlignment(HorizontalAlignment.CENTER);

			table.addCell(new Cell(0, 2).add("FASTag Details").setTextAlignment(TextAlignment.CENTER).setBold());
			table.addCell(new Cell().add("TID").setBold());
			table.addCell(new Cell().add(tid));
			table.addCell(new Cell().add(new Paragraph("TAG ID").add(asterisk)).setBold().setBold());
			table.addCell(new Cell().add(tagId));

			table.addCell(new Cell().add("Barcode Number").setBold());
			table.addCell(new Cell().add(barcodenumber));

			table.addCell(new Cell().add("Issuer Bank Name").setBold());
			table.addCell(new Cell().add("UPCB Bank")); //
			document.add(table);
			String linebreak = "\n";
			document.add(new Paragraph().add(linebreak));
			Table table2 = new Table(new float[] { 180F, 180F }).setHorizontalAlignment(HorizontalAlignment.CENTER);
			table2.addCell(new Cell(0, 2).add("Vehicle Details").setTextAlignment(TextAlignment.CENTER).setBold());
			table2.addCell(new Cell().add("Vehicle Registration Number").setBold());
			table2.addCell(new Cell().add(vehicleNumber));
			table2.addCell(new Cell().add("Engine Number").setBold());
			table2.addCell(new Cell().add(engineNumber));
			table2.addCell(new Cell().add(new Paragraph("Chassis Number").add(asterisk)).setBold());
			// table.addCell(new Cell().add("Chassis Number*").setBold());
			table2.addCell(new Cell().add(chassisNumber));
			document.add(table2);

			strBuilder = new StringBuilder();
			strBuilder.append("\n");
			strBuilder.append("\n");
			strBuilder.append("\n");
			strBuilder.append("\n");
			strBuilder.append("\n");
			strBuilder.append("\n");
			strBuilder.append("\n");
			strBuilder.append("Stamp & Signature Of Dealer/Agent");
			strBuilder.append("\n");
			strBuilder.append("OR");
			strBuilder.append("\n");
			strBuilder.append("Signature Of Customer");
			strBuilder.append("\n");
			strBuilder.append("\n");
			strBuilder.append("\n");
			// strBuilder.append("\n");
			// strBuilder.append("\n");
			document.add(new Paragraph(strBuilder.toString()).setTextAlignment(TextAlignment.RIGHT).setFontSize(14));
			String bottom = "*Field marked(*) are mandatory information to be provided in the challan.";
			document.add(new Paragraph(bottom).setTextAlignment(TextAlignment.CENTER).setFontSize(11));
			String bottom2 = "*Vehicle owner shall be responsible affixing FASTag applied through online channels.";
			document.add(new Paragraph(bottom2).setTextAlignment(TextAlignment.CENTER).setFontSize(11));

			// Closing the document
			document.close();
			
		} finally {
			document.close();
			document = null;
			writer.close();
			writer = null;
			
		}

	}

}
