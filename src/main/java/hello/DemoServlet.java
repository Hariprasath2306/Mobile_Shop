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

public class DemoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DemoServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wt1", "root", "root");

            PreparedStatement ps = con.prepareStatement("SELECT * FROM products");
            ResultSet rs = ps.executeQuery();

            int stock1 = 0;
            int stock2 = 0;

            while (rs.next()) {
                String productName = rs.getString("product_name");
                int availableCount = rs.getInt("available_count");

                if (productName.equals("Item 1")) {
                    stock1 = availableCount;
                } else if (productName.equals("Item 2")) {
                    stock2 = availableCount;
                }
            }

            con.close();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head>");
            out.println("    <meta charset=\"UTF-8\">");
            out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("    <title>Online Shop</title>");
            out.println("    <style>");
            out.println("        body {");
            out.println("            font-family: Arial, sans-serif;");
            out.println("            background-color: #f5f5f5;");
            out.println("            margin: 0;");
            out.println("            padding: 0;");
            out.println("        }");
            out.println("        header {");
            out.println("            background-color: #333;");
            out.println("            color: white;");
            out.println("            text-align: center;");
            out.println("            padding: 10px;");
            out.println("        }");
            out.println("        main {");
            out.println("            max-width: 800px;");
            out.println("            margin: 20px auto;");
            out.println("            padding: 20px;");
            out.println("            background-color: white;");
            out.println("            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);");
            out.println("        }");
            out.println("        .item {");
            out.println("            text-align: center;");
            out.println("            margin: 20px;");
            out.println("            padding: 10px;");
            out.println("            border: 1px solid #ccc;");
            out.println("            border-radius: 5px;");
            out.println("        }");
            out.println("        .buy-button {");
            out.println("            background-color: #333;");
            out.println("            color: white;");
            out.println("            border: none;");
            out.println("            padding: 5px 10px;");
            out.println("            cursor: pointer;");
            out.println("        }");
            out.println("    </style>");
            out.println("</head>");
            out.println("<body>");
            out.println("    <header>");
            out.println("        <h1>Samsung Store</h1>");
            out.println("    </header>");
            out.println("    <main>");
            out.println("        <form action=\"\" method=\"POST\">");
            out.println("            <center><label for=\"username\">Username:</label></center><br>");
            out.println("            <center><input type=\"text\" id=\"username\" name=\"username\"></center><br>");
            out.println("            <input type=\"hidden\" name=\"item1\" value=\"Item 1\">");
            out.println("            <input type=\"hidden\" name=\"item2\" value=\"Item 2\">");
            out.println("            <section class=\"item\">");
            out.println("                <h2>Samsung Galaxy S23 Ultra</h2>");
            out.println("                <p>Dynamic AMOLED 2X delivers clear details onscreen, whether dimmed down low or full-blast brightness.9 And 120Hz technology intelligently optimizes the refresh rate to smooth out action and save on battery.</p>");
            out.println("                <p>Price: ₹124,999.00</p>");
            out.println("                <p>Stock Available: <span id=\"stock1\">" + stock1 + "</span></p>");
            out.println("                <button class=\"buy-button\" type=\"submit\" name=\"purchaseItem\" value=\"Item 1\">Buy Now</button>");
            out.println("            </section>");
            out.println("            <section class=\"item\">");
            out.println("                <h2>Samsung Galaxy A73 5g</h2>");
            out.println("                <p>Do more of everything now. Powered with the Snapdragon® 778G 5G, Galaxy A73 5G completely changes your mobile multimedia lifestyle with pro-level gaming, accelerated AI for smarter performance and premium capture experiences. When you need more, RAM Plus provides an extra virtual RAM boost.</p>");
            out.println("                <p>Price: ₹30,999.00</p>");
            out.println("                <p>Stock Available: <span id=\"stock2\">" + stock2 + "</span></p>");
            out.println("                <button class=\"buy-button\" type=\"submit\" name=\"purchaseItem\" value=\"Item 2\">Buy Now</button>");
            out.println("            </section>");
            out.println("        </form>");
            out.println("    </main>");
            out.println("    <script>");
            out.println("        function buyItem(itemName, itemPrice) {");
            out.println("            alert(`You purchased ${itemName} for ₹${itemPrice.toFixed(2)}.`);");
            out.println("            const stockElement = document.getElementById(`stock${itemName.charAt(itemName.length - 1)}`);");
            out.println("            const currentStock = parseInt(stockElement.innerText);");
            out.println("            stockElement.innerText = currentStock - 1;");
            out.println("        }");
            out.println("    </script>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String purchasedItem = request.getParameter("purchaseItem");

        if (username != null && !username.isEmpty() && purchasedItem != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wt1", "root", "root");

                PreparedStatement ps = con.prepareStatement("SELECT available_count FROM products WHERE product_name=?");
                ps.setString(1, purchasedItem);
                ResultSet rs = ps.executeQuery();

                int availableCount = 0;

                if (rs.next()) {
                    availableCount = rs.getInt("available_count");
                }

                if (availableCount > 0) {
                    ps = con.prepareStatement("UPDATE products SET available_count=? WHERE product_name=?");
                    ps.setInt(1, availableCount - 1);
                    ps.setString(2, purchasedItem);
                    ps.executeUpdate();

                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();

                    if(purchasedItem.compareTo("Item 1") == 0) {
                        String proname = "Samsung S23 Ultra";
                        String price = "124,999.00	";
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Invoice</title>");
                        out.println("<style>");
                        out.println("body {");
                        out.println("    font-family: Arial, sans-serif;");
                        out.println("}");
                        out.println(".container {");
                        out.println("    max-width: 600px;");
                        out.println("    margin: 0 auto;");
                        out.println("    padding: 20px;");
                        out.println("    border: 1px solid #ccc;");
                        out.println("    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);");
                        out.println("}");
                        out.println("h1 {");
                        out.println("    text-align: center;");
                        out.println("}");
                        out.println("table {");
                        out.println("    width: 100%;");
                        out.println("    border-collapse: collapse;");
                        out.println("    margin-top: 20px;");
                        out.println("}");
                        out.println("th, td {");
                        out.println("    border: 1px solid #ccc;");
                        out.println("    padding: 8px;");
                        out.println("    text-align: left;");
                        out.println("}");
                        out.println("th {");
                        out.println("    background-color: #f2f2f2;");
                        out.println("}");
                        out.println("</style>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<div class=\"container\">");
                        out.println("<h1>Bill</h1>");
                        out.println("<p><strong>Username:</strong> " + username + "</p>");
                        out.println("<table>");
                        out.println("<thead>");
                        out.println("<tr>");
                        out.println("<th>Item</th>");
                        out.println("<th>Description</th>");
                        out.println("<th>Price (₹)</th>");
                        out.println("</tr>");
                        out.println("</thead>");
                        out.println("<tbody>");
                        out.println("<tr>");
                        out.println("<td>" + purchasedItem + "</td>");
                        out.println("<td>Samsung S23 Ultra</td>");
                        out.println("<td>₹124,999.00</td>");
                        out.println("</tr>");
                        out.println("</tbody>");
                        out.println("</table>");
                        out.println("<p><strong>Total Amount (₹):</strong> ₹124,999.00</p>");
                        out.println("</div>");
                        out.println("</body>");
                        out.println("</html>");
                    } else if (purchasedItem.compareTo("Item 2") == 0) {
                        String proname = "Samsung A73 5G";
                        String price = "34,999.00";
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Invoice</title>");
                        out.println("<style>");
                        out.println("body {");
                        out.println("    font-family: Arial, sans-serif;");
                        out.println("}");
                        out.println(".container {");
                        out.println("    max-width: 600px;");
                        out.println("    margin: 0 auto;");
                        out.println("    padding: 20px;");
                        out.println("    border: 1px solid #ccc;");
                        out.println("    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);");
                        out.println("}");
                        out.println("h1 {");
                        out.println("    text-align: center;");
                        out.println("}");
                        out.println("table {");
                        out.println("    width: 100%;");
                        out.println("    border-collapse: collapse;");
                        out.println("    margin-top: 20px;");
                        out.println("}");
                        out.println("th, td {");
                        out.println("    border: 1px solid #ccc;");
                        out.println("    padding: 8px;");
                        out.println("    text-align: left;");
                        out.println("}");
                        out.println("th {");
                        out.println("    background-color: #f2f2f2;");
                        out.println("}");
                        out.println("</style>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<div class=\"container\">");
                        out.println("<h1>Bill</h1>");
                        out.println("<p><strong>Username:</strong> " + username + "</p>");
                        out.println("<table>");
                        out.println("<thead>");
                        out.println("<tr>");
                        out.println("<th>Item</th>");
                        out.println("<th>Description</th>");
                        out.println("<th>Price (₹)</th>");
                        out.println("</tr>");
                        out.println("</thead>");
                        out.println("<tbody>");
                        out.println("<tr>");
                        out.println("<td>" + purchasedItem + "</td>");
                        out.println("<td>Samsung Galaxy A73 5G</td>");
                        out.println("<td>₹30,999.00</td>");
                        out.println("</tr>");
                        out.println("</tbody>");
                        out.println("</table>");
                        out.println("<p><strong>Total Amount (₹):</strong> ₹30,999.00</p>");
                        out.println("</div>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                } else {
                    // Item is out of stock
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<html>");
                    out.println("<head><title>Out of Stock</title></head>");
                    out.println("<body>");
                    out.println("<h1>The selected item is out of stock.</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }

                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            // Invalid username or item
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head><title>Invalid Username or Item</title></head>");
            out.println("<body>");
            out.println("<h1>Please provide a valid username and select an item.</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
