<?php 

$json="";
$host="localhost"; //replace with database hostname  

$username="root"; //replace with database username  

$password="golden786"; //replace with database password  

$db_name="android_api"; //replace with database name   


$conn = new mysqli($host, $username, $password, $db_name);

// Check connection
 if ($conn->connect_error) {
     die("Connection failed: " . $conn->connect_error);
} 


$sql = "select * from COMPLAIN";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
       $json['COMPLAIN'][]=$row;
    }
} else {
    
}


$conn->close();
echo json_encode($json);  
?>
