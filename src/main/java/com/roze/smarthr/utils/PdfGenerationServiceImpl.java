package com.roze.smarthr.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.roze.smarthr.entity.OfferLetter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PdfGenerationServiceImpl implements PdfGenerationService{

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

    public byte[] generateOfferLetterPdf(OfferLetter offerLetter) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add company logo (you'll need to implement this)
            // Image logo = Image.getInstance(getClass().getResource("/static/images/logo.png"));
            // logo.scaleToFit(100, 100);
            // document.add(logo);

            // Title
            Paragraph title = new Paragraph("OFFER LETTER", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Date
            Paragraph date = new Paragraph(
                    "Date: " + offerLetter.getIssueDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    NORMAL_FONT);
            date.setAlignment(Element.ALIGN_RIGHT);
            document.add(date);

            // Candidate info
            document.add(new Paragraph(" ")); // Spacer
            document.add(new Paragraph("To:", NORMAL_FONT));
            document.add(new Paragraph(offerLetter.getCandidate().getFullName(), BOLD_FONT));
            document.add(new Paragraph(offerLetter.getCandidate().getEmail(), NORMAL_FONT));
            document.add(new Paragraph(offerLetter.getCandidate().getPhone(), NORMAL_FONT));

            document.add(new Paragraph(" ")); // Spacer

            // Salutation
            document.add(new Paragraph("Dear " + offerLetter.getCandidate().getFullName() + ",", NORMAL_FONT));

            // Main content
            Paragraph content = new Paragraph();
            content.add(new Paragraph(
                    "We are pleased to offer you the position of " + offerLetter.getOfferedPosition() + 
                    " at our company, effective " + 
                    offerLetter.getJoiningDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) + ".", 
                    NORMAL_FONT));
            content.add(new Paragraph(" ")); // Spacer
            
            // Salary details
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);
            
            addTableHeader(table, "Compensation Details");
            addTableRow(table, "Position:", offerLetter.getOfferedPosition());
            addTableRow(table, "Annual Salary:", "$" + offerLetter.getSalaryOffered());
            addTableRow(table, "Joining Date:", 
                offerLetter.getJoiningDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            
            document.add(table);

            // Terms and conditions
            if (offerLetter.getTermsAndConditions() != null && !offerLetter.getTermsAndConditions().isEmpty()) {
                document.add(new Paragraph("Terms and Conditions:", HEADER_FONT));
                document.add(new Paragraph(offerLetter.getTermsAndConditions(), NORMAL_FONT));
            }

            // Closing
            document.add(new Paragraph(" ")); // Spacer
            document.add(new Paragraph(
                    "We look forward to you joining our team. Please sign and return a copy of this letter to indicate your acceptance of this offer.",
                    NORMAL_FONT));
            document.add(new Paragraph(" ")); // Spacer
            document.add(new Paragraph("Sincerely,", NORMAL_FONT));
            document.add(new Paragraph(" ")); // Spacer
            document.add(new Paragraph(offerLetter.getIssuedBy().getUsername(), BOLD_FONT));
            document.add(new Paragraph("HR Department", NORMAL_FONT));
            document.add(new Paragraph(offerLetter.getIssuedBy().getEmail(), NORMAL_FONT));

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF offer letter", e);
            throw new RuntimeException("Failed to generate PDF offer letter", e);
        }
    }

    private void addTableHeader(PdfPTable table, String header) {
        PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(220, 220, 220));
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, BOLD_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }
}