package com.inn.cafe.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Bill;
import com.inn.cafe.constants.CafeConstant;
import com.inn.cafe.dao.BillDao;
import com.inn.cafe.service.BillService;
import com.inn.cafe.utils.CafeUtil;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillServiceImpl implements BillService{

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<String> generatedReport(Map<String, Object> requestMap) {
        try {
            String fileName;
            if (validateRequestMap(requestMap)) {
                            if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                                fileName = (String) requestMap.get("uuid");
                            } else {
                                fileName= CafeUtil.getUUID();
                                requestMap.put("uuid", fileName);
                                insertBill(requestMap);
                            }
                            String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: " +requestMap.get("contactNumber")+ "\n" +"Email: "+ requestMap.get("email")+"\n" + "Payement Method :" + requestMap.get("payementMethod");
                            Document document = new Document();
                            PdfWriter.getInstance(document, new FileOutputStream(CafeConstant.STORE_LOCATION + "//" + fileName + ".pdf"));
                            document.open();
                            setRectangleInPdf(document);

                            Paragraph chunk = new Paragraph("Cafe Management System",getFont("Header"));
                            chunk.setAlignment(Element.ALIGN_CENTER);
                            document.add(chunk);

                            Paragraph paragraph = new Paragraph(data + "\n \n",getFont("Data"));
                            document.add(paragraph);

                            PdfPTable table = new PdfPTable(5);
                            table.setWidthPercentage(100);
                            addTableHeader(table);

                            JSONArray jsonArray= CafeUtil.getJsonArrayFromString((String) requestMap.get("Product Details"));
                            for (int i = 0; i < jsonArray.length();i++) {
                                addRows(table, CafeUtil.getMapFromJSON(jsonArray.getString(i)));
                            }
                            document.add(table);

                            Paragraph footer = new Paragraph("Total: " + requestMap.get("totalAmount") + "\\n" + "Thank you for visiting. Please visit again!! ",getFont(data));
                            document.add(footer);
                            document.close();
                            return new ResponseEntity<>("{\"uuid \": \"" + fileName+"\"}",HttpStatus.OK);
                        } else {
                            return CafeUtil.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            
         private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont =FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
            Font dataFont=FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
            dataFont.setStyle(Font.BOLD);
            return dataFont;
        
            default:
                return new Font();
        }
    }

        private boolean validateRequestMap(Map<String, Object> requestMap) {
                return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("payementMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
         }
         private void setRectangleInPdf(Document document) throws DocumentException {
            log.info("Inside setRectanglePdf");
            Rectangle rect = new Rectangle(577, 825, 18, 15);
            rect.enableBorderSide(1);
            rect.enableBorderSide(2);
            rect.enableBorderSide(4);
            rect.enableBorderSide(8);
            rect.setBorderColor(BaseColor.BLACK);
            rect.setBorderWidth(1);
            document.add(rect);
        }
            
         private void insertBill(Map<String, Object> requestMap) {
            try {
                Bill bill = new Bill();

            bill.setUuid((String) requestMap.get("uuid"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setName((String) requestMap.get("name"));
            bill.setPayementMethod((String) requestMap.get("payementMethod"));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("total")));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void addTableHeader(PdfPTable table) {
            log.info("Inside addTableHeader");
            Stream.of("Name","Category","Quantity","Price","Sub Total")
            .forEach(columnTitle->{
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.BLACK);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(columnTitle));
                header.setBackgroundColor(BaseColor.YELLOW);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(header);
            });

        }

        private void addRows(PdfPTable table, Map<String,Object> data) {
            log.info("Inside addRows");
            table.addCell((String) data.get("name"));
            table.addCell((String) data.get("category"));
            table.addCell((String) data.get("quantity"));
            table.addCell(Double.toString((Double) data.get("price")));
            table.addCell(Double.toString((Double) data.get("total")));
        }

        @Override
        public ResponseEntity<List<Bill>> getBills() {
            List<Bill> list = new ArrayList<>();
            if (jwtFilter.isAdmin()) {
                list = billDao.getAllBills();
            } else {
                list = billDao.getBillByUserName(jwtFilter.getCurrentUser());
            }
            return new ResponseEntity<>(list,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Override
        public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
            log.info("Inside getPdf: requestMap {}", requestMap);
            try {
                byte[] byteArray = new byte[0]; 
                if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) 
                    return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
                String filePath = CafeConstant.STORE_LOCATION + "\\" + (String) requestMap.get("uuid")+ ".pdf";
                if (CafeUtil.isFileExist(filePath)) {
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray,HttpStatus.OK);
                }else{
                    requestMap.put("isGenerate", false);
                    generatedReport(requestMap);
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray,HttpStatus.OK);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private byte[] getByteArray(String filePath) throws Exception{
            File initialFile = new File(filePath);
            InputStream targStream = new FileInputStream(initialFile);
            byte[] byteArray = IOUtils.toByteArray(targStream);
            targStream.close();
            return byteArray;
        }

        @Override
        public ResponseEntity<String> deleteBill(Integer id) {
            try {
                Optional<Bill> optional = billDao.findById(id);
                if (!optional.isEmpty()) {
                    billDao.deleteById(id);
                    return CafeUtil.getResponseEntity("Bill deleted successfully.", HttpStatus.OK);
                } else {
                    return CafeUtil.getResponseEntity("Bill id doesn't exist.", HttpStatus.OK);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
