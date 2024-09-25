<?php
$servername = "localhost"; // Replace with your database server name if not localhost
$username = "root";        // Replace with your MySQL username
$password = "";            // Replace with your MySQL password
$dbname = "cricket_tournament"; // Replace with your database name

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = mysqli_real_escape_string($conn, $_POST['username']);
    $email = mysqli_real_escape_string($conn, $_POST['email']);
    $phone = mysqli_real_escape_string($conn, $_POST['phone']);
    $clubname = mysqli_real_escape_string($conn, $_POST['clubname']);
    $teamname = mysqli_real_escape_string($conn, $_POST['teamname']);

    $sql = "INSERT INTO registrations (username, email, phone, clubname, teamname)
            VALUES ('$username', '$email', '$phone', '$clubname', '$teamname')";

    if ($conn->query($sql) === TRUE) {
        echo "Registration successful!";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}

$conn->close();
?>