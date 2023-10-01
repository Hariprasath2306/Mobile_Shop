package hello;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DemoSecond extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wt1", "root", "root");

            PreparedStatement ps = con.prepareStatement("SELECT * FROM products");
            ResultSet rs = ps.executeQuery();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head>");
            out.println("    <meta charset=\"UTF-8\">");
            out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("    <title>Stock Management</title>");
            out.println("    <style>");
            out.println("        body {");
            out.println("            font-family: Arial, sans-serif;");
            out.println("            background-color: #f5f5f5;");
            out.println("            margin: 0;");
            out.println("            padding: 0;");
            out.println("        }");
            out.println("        table {");
            out.println("            border-collapse: collapse;");
            out.println("            width: 50%;");
            out.println("            margin: 50px auto;");
            out.println("        }");
            out.println("        th, td {");
            out.println("            border: 1px solid #ccc;");
            out.println("            padding: 10px;");
            out.println("            text-align: center;");
            out.println("        }");
            out.println("        th {");
            out.println("            background-color: #f2f2f2;");
            out.println("        }");
            out.println("        input {");
            out.println("            padding: 5px;");
            out.println("            width: 50px;");
            out.println("            text-align: center;");
            out.println("        }");
            out.println("        button {");
            out.println("            padding: 5px 10px;");
            out.println("        }");
            out.println("    </style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1 style=\"text-align:center;\">Stock Management</h1>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Mobiles</th>");
            out.println("<th>Available Count</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");
            
            while (rs.next()) {
                String productName = rs.getString("product_name");
                int availableCount = rs.getInt("available_count");
                
                out.println("<tr>");
                out.println("<td>" + productName + "</td>");
                out.println("<td><input type=\"text\" id=\"" + productName + "\" value=\"" + availableCount + "\"></td>");
                out.println("<td><button onclick=\"updateStock('" + productName + "')\">Update</button></td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<script>");
            out.println("    function updateStock(productName) {");
            out.println("        var newCount = document.getElementById(productName).value;");
            out.println("        var form = document.createElement('form');");
            out.println("        form.method = 'POST';");
            out.println("        form.action = ''; // Leave action empty to submit to the current URL");
            out.println("        var productNameField = document.createElement('input');");
            out.println("        productNameField.type = 'hidden';");
            out.println("        productNameField.name = 'productName';");
            out.println("        productNameField.value = productName;");
            out.println("        var newCountField = document.createElement('input');");
            out.println("        newCountField.type = 'hidden';");
            out.println("        newCountField.name = 'newCount';");
            out.println("        newCountField.value = newCount;");
            out.println("        form.appendChild(productNameField);");
            out.println("        form.appendChild(newCountField);");
            out.println("        document.body.appendChild(form);");
            out.println("        form.submit();");
            out.println("    }");
            out.println("</script>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productName = request.getParameter("productName");
        int newCount = Integer.parseInt(request.getParameter("newCount"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wt1", "root", "root");

            PreparedStatement ps = con.prepareStatement("UPDATE products SET available_count=? WHERE product_name=?");
            ps.setInt(1, newCount);
            ps.setString(2, productName);
            int updatedRows = ps.executeUpdate();

            con.close();
            if (updatedRows > 0) {
            	response.getWriter().write("<script>alert('Stock updated successfully.');</script>");
            } else {
                response.getWriter().write("Failed to update stock.");
            }

        } catch (Exception e) {
            System.out.println(e);
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }
}