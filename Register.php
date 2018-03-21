<?php
   $con = mysqli_connect("idpparking.000webhostapp.com", "id5133504_idpadmin", "idpadmin", "id5133504_user");
    $userName = $_POST["userName"];
    $password = $_POST["password"];
    $license = $_POST["license"];
    $firstName = $_POST["firstName"];
    $lastName = $_POST["lastName"];

    $statement = mysqli_prepare($con, "INSERT INTO user (userName, password, license, firstName, lastName) VALUES (?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "siss", $userName, $password, $license, $firstName, $lastName);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>
