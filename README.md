
---

### Bonus: Simple HTML with embedded CSS (for a project page)

```html
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>POS System Documentation</title>
<style>
  body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background: #f9f9f9;
    color: #333;
    margin: 2rem;
    line-height: 1.6;
  }
  h1, h2 {
    color: #2c3e50;
  }
  hr {
    border: none;
    border-top: 2px solid #eee;
    margin: 1.5rem 0;
  }
  code {
    background: #eaeaea;
    padding: 0.2rem 0.4rem;
    border-radius: 4px;
    font-family: monospace;
  }
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
  }
  th, td {
    border: 1px solid #ddd;
    padding: 0.75rem;
    text-align: left;
  }
  th {
    background: #3498db;
    color: white;
  }
  ul {
    list-style: inside disc;
  }
</style>
</head>
<body>

<h1>POS System</h1>

<h2>Team Members</h2>
<ul>
  <li><strong>Srun Borath</strong></li>
  <li><strong>Sov YongKhang</strong></li>
  <li><strong>Sok Socheata</strong></li>
</ul>

<hr />

<h2>Steps to Run the Project</h2>

<h3>Step 1: Clone the Project</h3>
<pre><code>git clone https://github.com/Srunborath7/pos_system_java.git</code></pre>

<h3>Step 2: Run the Project</h3>
<ul>
  <li>Open the project in your IDE (IntelliJ, Eclipse, etc.).</li>
  <li>Build and run the Spring Boot application.</li>
  <li>The app will start on port <code>8080</code> by default.</li>
</ul>

<hr />

<h2>How to Test the APIs</h2>
<p>Use <strong>Postman</strong> or any API client to test the endpoints:</p>

<table>
  <thead>
    <tr>
      <th>Endpoint</th>
      <th>HTTP Method</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>http://localhost:8080/api/users</td>
      <td>GET</td>
      <td>Retrieve all users</td>
    </tr>
    <tr>
      <td>http://localhost:8080/api/products</td>
      <td>GET</td>
      <td>Retrieve all products</td>
    </tr>
    <tr>
      <td>http://localhost:8080/api/categories</td>
      <td>GET</td>
      <td>Retrieve all categories</td>
    </tr>
  </tbody>
</table>

<hr />

<h2>Database Access</h2>
<ul>
  <li>The project uses an <strong>H2 in-memory database</strong>.</li>
  <li>Access the H2 console at: <a href="http://localhost:8080/h2-console" target="_blank">http://localhost:8080/h2-console</a></li>
  <li>Default JDBC URL: <code>jdbc:h2:mem:testdb</code> (verify in your config)</li>
  <li>Username and password are usually blank or as configured.</li>
</ul>

<hr />

<h2>Notes</h2>
<ul>
  <li>Ensure port <code>8080</code> is free before running the app.</li>
  <li>Currently, APIs provide <strong>read-only</strong> data access.</li>
  <li>H2 console is for development use only.</li>
</ul>

</body>
</html>
