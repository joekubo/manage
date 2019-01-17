
package com.tolclin.manage;
//--------------------------------------------------------------------------------------------------Javaconnect Libs------
import java.sql.DriverManager;
//--------------------------------------------------------------------------------------------------end Javaconnect Libs--
import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import org.json.JSONArray;

public class Manage {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    PreparedStatement pst1 = null;
    ResultSet rs1 = null;
    PreparedStatement pst2 = null;
    ResultSet rs2 = null;
    PreparedStatement pst3 = null;
    ResultSet rs3 = null;
    String path = null;
    String filename;
    String s;
    
    public Manage(){
        conn = Manage.ConnecrDb();
    }
    public static Connection ConnecrDb(){
         try{ 
             Class.forName("com.mysql.jdbc.Driver");
             Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshopsoftdb","root","JesusChrist1");  
             return conn;     
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+("Please Check on the Network Connection."
                     + " This Computer must be in the same Network with the Server..."));
             System.exit(0);
             return null;
         }   
     }
    
    public int totalqty(String sql,String value){
        int qty = 0;
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    qty = rs.getInt(value);
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" manage.totalqty");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        return qty;
    }
    public int checking(String sql){
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    return 1;
                }else{
                    return 0;
                }
        }catch(Exception e){
            System.out.println(e+" manage.checking");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        return 2;
    }
    public void update(String sql){
        try{
            pst = conn.prepareStatement(sql);
            pst.execute();
        }catch(Exception e){
            System.out.println(e+" manage.update");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void showdialog(JDialog dialog, JPanel panel, int x, int y){
        dialog.setVisible(true);
        dialog.setSize(x, y);
        dialog.setResizable(false);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(panel);
    }
    
    public void delete(String sql){
        try{
            pst = conn.prepareStatement(sql);
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" delete");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void update_table(String sql, JTable table){
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(rs));
            TableColumn idColumn = table.getColumn("id");
            idColumn.setMaxWidth(0);
            idColumn.setMinWidth(0);
            idColumn.setPreferredWidth(0);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" manage.update_table");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    
    public void save(String sql,int number,String field){
        try{
            for(int i = 0; i < number; i++){
                String value = field+""+i;
                pst = conn.prepareStatement(sql);
                
                pst.setString(i, value);
                pst.execute();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e+" manage.save");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void fillcombo(String sql, JComboBox combobox,String value){
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                while(rs.next()){
                    combobox.addItem(rs.getString(""+value+""));
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" fillcombo");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void loggedinmessage(String loggedid_number, String loggedusername){
        try{
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String operation = "ID No: "+loggedid_number+", NAME: "+loggedusername+" is logged in at "+timeFormat.format(date.getTime())+"";
            String sql = "INSERT INTO logstable(operation,s,date)VALUES(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, operation);
            pst.setString(2, "1");
            pst.setString(3, dateFormat.format(date.getTime()));
            pst.execute();
            String query = "UPDATE users_table SET logged = '1' WHERE id_number = '"+loggedid_number+"'";
            this.update(query);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" loggedinmessage");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void loggedoutmessage(String loggedid_number,String loggedusername){
        try{
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String operation = "ID No: "+loggedid_number+", NAME: "+loggedusername+" has logged out at "+timeFormat.format(date.getTime())+"";
            String sql = "INSERT INTO logstable(operation,s,date)VALUES(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, operation);
            pst.setString(2, "1");
            pst.setString(3, dateFormat.format(date.getTime()));
            pst.execute();
            String query = "UPDATE users_table SET logged = '0' WHERE id_number = '"+loggedid_number+"'";
            this.update(query);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" loggedinmessage");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void loggedmessageupdate(String name, String loggedid_number,String loggedusername){
        try{
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String operation = "ID No: "+loggedid_number+", NAME: "+loggedusername+", Updated "+name+" ON "+dateFormat.format(date.getTime())+" AT  "
                                                                                                                + ""+timeFormat.format(date.getTime())+"";
            String sql = "INSERT INTO logstable(operation,s,date)VALUES(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, operation);
            pst.setString(2, "1");
            pst.setString(3, dateFormat.format(date.getTime()));
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" loggedmessageupdate");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void loggedmessagedelete(String name,String loggedid_number,String loggedusername){
        try{
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String operation = "ID No: "+loggedid_number+", NAME: "+loggedusername+" Deleted "+name+" details ON "+dateFormat.format(date.getTime())+" "
                                                                                                                + "AT "+timeFormat.format(date.getTime())+"";
            String sql = "INSERT INTO logstable(operation,s,date)VALUES(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, operation);
            pst.setString(2, "1");
            pst.setString(3, dateFormat.format(date.getTime()));
            
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" loggedmessagedelete");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void report(String sql,String path,JPanel panel){
        try {
            JasperDesign jd = JRXmlLoader.load(path);
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jd.setQuery(newQuery);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.repaint();
            panel.add(new JRViewer(jp));
            panel.revalidate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e+" report");

        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {

            }
        }
    } 
  //____________________________________________________________________________________________end backing up database_________________________________________________________  
    
    public void sendemail(String sender,String to,String pass,String companyname){
        final String username = sender;
        final String password = pass;

         Properties props = new Properties();
         props.put("mail.smtp.auth", true);
         props.put("mail.smtp.starttls.enable", true);
         props.put("mail.smtp.host", "smtp.gmail.com");
         props.put("mail.smtp.port", "587");

         Session session = Session.getInstance(props,
                 new javax.mail.Authenticator() {
                     protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                         return new javax.mail.PasswordAuthentication(username, password);
                     }
                 });

         try {

             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress("josephmwawasi29@gmail.com"));
             message.setRecipients(Message.RecipientType.TO,
                     InternetAddress.parse(to));
             message.setSubject(companyname+" Db Backup");
             message.setText("Security system at "+companyname+" database attached...");

             MimeBodyPart messageBodyPart = new MimeBodyPart();

             Multipart multipart = new MimeMultipart();

             messageBodyPart = new MimeBodyPart();
             String file = path;
             //String fileName = companyname+" Database Backup";
             DataSource source = new FileDataSource(file);
             messageBodyPart.setDataHandler(new DataHandler(source));
             messageBodyPart.setFileName(path);
             multipart.addBodyPart(messageBodyPart);

             message.setContent(multipart);

             System.out.println("Sending...");

             Transport.send(message);

             System.out.println("Done");

         } catch (MessagingException e) {
             
         }
    }
    public void poison(JPanel panel,String end_date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 0);
            String setdate = end_date;//end_date is you set the date which you want the system to de-activate
            String currentdate = sdf.format(c.getTime());
                try{
                    Date set = sdf.parse(setdate);
                    Date now = sdf.parse(currentdate);
                    
                    if(now.after(set)){
                         panel.setVisible(false);//panel used here is desktop
                    }
                }catch(Exception e){
                    
                }
            
    }
    //validating jtextfield's length
    public  boolean length_check(String in,int len){
        return in.length() > len;
    }
    
   public int total_qty(String sql, String value_string){
       int total_qty = 0;
       try{
           pst = conn.prepareStatement(sql);
           rs = pst.executeQuery();
            if(rs.next()){
                total_qty = rs.getInt(""+value_string+"");
            }
       }catch(Exception e){
           System.out.println(e+" manage.total_qty");
       }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception e){
               
           }
       }
       return total_qty;
   }
   //sendemail_companyupdate(josephmwawasi29@gmail.com","tolclin.it@gmail.com","J35u5Christ",companyname,accountname,location,address,city,phoneno,email,sql,softwarename);
   public void sendemail_companyupdate(String sender,String to,String pass,String companyname,String accountname,String location,String address,String city
           ,String phoneno,String email,String sql,String softwarename){
        final String username = sender;
        final String password = pass;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Date date = new Date();

         Properties props = new Properties();
         props.put("mail.smtp.auth", true);
         props.put("mail.smtp.starttls.enable", true);
         props.put("mail.smtp.host", "smtp.gmail.com");
         props.put("mail.smtp.port", "587");

         Session session = Session.getInstance(props,
                 new javax.mail.Authenticator() {
                     protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                         return new javax.mail.PasswordAuthentication(username, password);
                     }
                 });

         try {

             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress("josephmwawasi29@gmail.com"));
             message.setRecipients(Message.RecipientType.TO,
                     InternetAddress.parse(to));
             message.setSubject("ALERT!!! "+softwarename+"-"+accountname+" INSTALLATION/UPDATE TAKING PLACE at "+companyname+"");
             message.setText("Installation or Update done to "+softwarename+"-"+accountname+" in "+companyname+" located at "+location+", Address "+address+""
                     + ", in "+city+", Phone # "+phoneno+" and Email Address "+email+" AT '"+dateFormat.format(date)+"'"
                             + "... Are you Aware?");
             System.out.println("Sending");
             Transport.send(message);
             System.out.println("Done");
             update(sql);
             JOptionPane.showMessageDialog(null, "Company Details Updated Successfully...");
             
         } catch (MessagingException e) {
             JOptionPane.showMessageDialog(null, "Cannot Update Company details without Internet Connection.Please Connect to internet...");
         }
    }
    public int totalstock_qty(String product_code, String company_id){
        int total_stockqty = 0;
        try{
           
            String sql = "SELECT COALESCE(SUM(qty), 0) FROM purchases_table WHERE s = '1'AND company_id = '"+company_id+"' AND product_code = '"+product_code+"'";
            String sql1 = "SELECT COALESCE(SUM(qty), 0) FROM remove_table WHERE s = '1' AND company_id = '"+company_id+"' AND product_code = '"+product_code+"'";
            String sql2 = "SELECT COALESCE(SUM(qty), 0) FROM sales_table WHERE s = '1' AND company_id = '"+company_id+"' AND product_code = '"+product_code+"'";
            String sql3 = "SELECT COALESCE(SUM(qty), 0) FROM invoice_info WHERE s = '1' AND company_id = '"+company_id+"' AND product_code = '"+product_code+"'";
            
            int purchaseqty;
            int removeqty = 0;
            int salesqty = 0;
            int invoiceqty = 0;
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    purchaseqty = rs.getInt("COALESCE(SUM(qty), 0)");
                    pst1 = conn.prepareStatement(sql1);
                    rs1 = pst1.executeQuery();
                        if(rs1.next()){
                            removeqty = rs1.getInt("COALESCE(SUM(qty), 0)");
                            pst2 = conn.prepareStatement(sql2);
                            rs2 = pst2.executeQuery();
                                if(rs2.next()){
                                    salesqty = rs2.getInt("COALESCE(SUM(qty), 0)");
                                        pst3 = conn.prepareStatement(sql3);
                                        rs3 = pst3.executeQuery();
                                            if(rs3.next()){
                                                invoiceqty = rs3.getInt("COALESCE(SUM(qty), 0)");
                                            }
                                }
                        }
                        total_stockqty = purchaseqty - (removeqty + salesqty + invoiceqty);
                        
                }
        }catch(Exception e){
            System.out.println(e+" purchasespanel.totalstock_qty");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        System.out.println(total_stockqty);
        return total_stockqty;
    }
    public String updatedate(String sql){
        try{
            pst = conn.prepareStatement(sql);
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+"manage.updatedate");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        return "UPDATED";
    }
    //______________________________________________________________________________________________backing up database______________________________________------securitydb-----
    public void setbackuppath(String companyname){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            
            try{
                path = companyname.replaceAll("\\s+","_")+"_"+ date +".sql";
            }catch(Exception e){ 
                System.out.println(e+" setbackuppath");
            }
    }
    public void backupnow(String username,String password,String database){
        Process p = null;
                    try{
                        Runtime runtime = Runtime.getRuntime();
                        p = runtime.exec("C:/xampp/mysql/bin/mysqldump.exe -u"+username+" -p"+password+" --add-drop-database -B "+database+" -r" +path);
                        int processComplete = p.waitFor();
                            if(processComplete == 0){
                                System.out.println("Backup created successfully...");
                            }else{
                                System.out.println("Backup Unsuccessfully...");
                            }
                    }catch(Exception e){
                            System.out.println(e+" backupnow");
                    }
    }
  //____________________________________________________________________________________________end backing up database_________________________________________________________  
    
    public void sendnotification_emailtome(String sender,String to,String pass,String subject_to_me, String message_to_me,JDialog dialog){
        final String username = sender;
        final String password = pass;

         Properties props = new Properties();
         props.put("mail.smtp.auth", true);
         props.put("mail.smtp.starttls.enable", true);
         props.put("mail.smtp.host", "smtp.gmail.com");
         props.put("mail.smtp.port", "587");

         Session session = Session.getInstance(props,
                 new javax.mail.Authenticator() {
                     protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                         return new javax.mail.PasswordAuthentication(username, password);
                     }
                 });

         try {

             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress("josephmwawasi29@gmail.com"));
             message.setRecipients(Message.RecipientType.TO,
                     InternetAddress.parse(to));
             message.setSubject(subject_to_me);//email subject
             message.setText(message_to_me);//email message
             
             System.out.println("Sending...");
             
             Transport.send(message);
             
             System.out.println("Done!");
         } catch (MessagingException e) {
             dialog.dispose();
             JOptionPane.showMessageDialog(null," Check on internet connection...Email was unable to be sent...");
         }
    }
    public void sendmessage(String recipients,String message){
        // Specify your login credentials
        String username = "tolclin";
        String apiKey   = "a134b86cef192b3e597957d96fe486f6dd887ba4592ecf31ed737c2f1407b348";
        // Specify the numbers that you want to send to in a comma-separated list
        // Please ensure you include the country code (+254 for Kenya in this case)
        //String recipients = "+254723095840";--------------------was active
        // And of course we want our recipients to know what we really do
        //String message = "We are tolclin IT. We code all day and sleep all night";--------------was active
        // Create a new instance of our awesome gateway class
        AfricasTalkingGateway gateway  = new AfricasTalkingGateway(username, apiKey);
        /*************************************************************************************
            NOTE: If connecting to the sandbox:
            1. Use "sandbox" as the username
            2. Use the apiKey generated from your sandbox application
                https://account.africastalking.com/apps/sandbox/settings/key
            3. Add the "sandbox" flag to the constructor
            AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey, "sandbox");
        **************************************************************************************/
        // Thats it, hit send and we'll take care of the rest. Any errors will
        // be captured in the Exception class below
        try {
            JSONArray results = gateway.sendMessage(recipients, message);
            //JOptionPane.showMessageDialog(null, "Message Sent Successfully...");
//            for( int i = 0; i < results.length(); ++i ) {
//                JSONObject result = results.getJSONObject(i);
//                //System.out.print(result.getString("status") + ","); // status is either "Success" or "error message"
//                System.out.print(result.getString("statusCode") + ",");
//                System.out.print(result.getString("number") + ",");
//                System.out.print(result.getString("messageId") + ",");
//                System.out.println(result.getString("cost"));
//                
//            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Encountered an error while sending. Check on your internet connection...");
        }
    }
    public Date expiry(){
        Date final_ = new Date();
         try{
            String sql = "SELECT final_date FROM renew_table WHERE s = '1'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    final_ = rs.getDate("final_date");
                }
        }catch(Exception e){
           System.out.println(e+" manage.expiry");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
         return final_;
    }
    public void music(String path){
        try{
            String soundName = path;    
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }catch(Exception e){
           System.out.println(e+" Sound Error!!!");
        }
    }
    public void sendemail_attachment(String email_from,String password,String email_to,String subject,String msg,String file_name){
      
    Properties props = new Properties();
//    props.put("mail.smtp.auth", true);
//    props.put("mail.smtp.starttls.enable", true);
//    props.put("mail.smtp.host", "mail.tolclin.com");
//    props.put("mail.smtp.port", "465");
props.put("mail.smtp.auth", true);
            props.put("mail.smtp.starttls.enable", true);
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email_from, password);
                }
            });

    try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email_from));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email_to));
        message.setSubject(subject);
        message.setText(msg);

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        Multipart multipart = new MimeMultipart();

        messageBodyPart = new MimeBodyPart();
        String file = "generated_report.pdf";
        String fileName = file_name;
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        System.out.println("Sending");

        Transport.send(message);

        System.out.println("Done");
        JOptionPane.showMessageDialog(null, "Sent Successfully...");

    } catch (MessagingException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error has occured during sending...Please Check your email password or your internet connection...");
    }
  }
     public void report_(String sql,String path,JPanel panel,String jasper_path){//panel report
        
        try {
            JasperDesign jd = JRXmlLoader.load(path);//Reports/invoice.jrxml
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jd.setQuery(newQuery);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            //JRDataSource jrDataSource = new JREmptyDataSource();
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn); 
            JasperExportManager.exportReportToPdfFile(jp,"generated_report.pdf");
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.repaint();
            panel.add(new JRViewer(jp));
            panel.revalidate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e+" manage.report_");
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {

            }
        }
    }
    private ImageIcon ResizeImage(String imgPath,JLabel label){
        //String imgPath = "";
        ImageIcon MyImage = new ImageIcon(imgPath);
        Image img = MyImage.getImage();
        Image newImage = img.getScaledInstance(label.getWidth(), label.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImage);
        return image;
    }
    public void filechooser(JLabel label){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg","gif","png");
        fileChooser.addChoosableFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                String pathe = selectedFile.getAbsolutePath();
                label.setIcon(ResizeImage(pathe,label));
                s = pathe;
              }
         else if(result == JFileChooser.CANCEL_OPTION){
             System.out.println("No Data");
         }
    }
    
    private float daysBetween;
    public void renewingtime(JComboBox comboamount, JLabel lbl_days, int amount_per_date){//activate immediately the code has accepted.
            String amount = comboamount.getSelectedItem().toString();
            amount = amount.trim();
            int renewal_amount = Integer.parseInt(amount);
            int days = renewal_amount/amount_per_date ;
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            //c.add(Calendar.DATE, days);
            daysBetween = 0;
                try{
                    String sql = "SELECT * FROM renew_table WHERE s = '1' AND id = '1'";
                    String sql1 = "SELECT final_date,today_date FROM renew_table WHERE s = '1' AND id = '1'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                        if(rs.next()){
                            Date final_d = rs.getDate("final_date");
                            Date initial_d = rs.getDate("today_date");
                            
                                if(initial_d == final_d || initial_d.after(final_d)){
                                    c.add(Calendar.DATE, days);
                                }else{
                                    //----------------------------date_difference-----------------
                                    pst1 = conn.prepareStatement(sql1);
                                    rs1 = pst1.executeQuery();
                                        if(rs1.next()){
                                            String dateBeforeString = rs1.getString("today_date");
                                            String dateAfterString = rs1.getString("final_date");
                                            Date dateBefore = sdf.parse(dateBeforeString);
                                            Date dateAfter = sdf.parse(dateAfterString);
                                            long difference = dateAfter.getTime() - dateBefore.getTime();
                                            daysBetween = (difference / (1000*60*60*24));
                                            lbl_days.setText(String.format("%.0f", daysBetween)+" Days left");
                                        }
                                    //----------------------------date_difference end ------------
                                    int daybtwn = (int)(Math.round(daysBetween));
                                    
                                    c.add(Calendar.DATE, days - (- daybtwn));
                                }
                                String output = sdf.format(c.getTime());
                                updatedate("UPDATE renew_table SET final_date = '"+output+"' WHERE id = '1'");
                        }
                }catch(Exception e){
                    System.out.println(e+" ");
                }finally{
                    try{
                        rs.close();
                        pst.close();
                    }catch(Exception e){

                    }
                }
    } 
    public void save(int n){
        try{
           
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" save");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
}

