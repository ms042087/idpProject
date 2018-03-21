<?php
    $con = mysqli_connect("idpparking.000webhostapp.com", "id5133504_idpadmin", "idpadmin", "id5133504_user");

    $userName = $_POST["userName"];
    $password = $_POST["password"];

    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE userName = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $userName, $password);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $userName , $password, $license$, $firstName, $lastName);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["userName"] = $userName;
        $response["password"] = $password;
        $response["license"] = $license;
        $response["firstName"] = $firstName;
        $response["lastName"] = $lastName;
    }

    echo json_encode($response);
?>
