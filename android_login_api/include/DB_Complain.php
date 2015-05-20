<?php
  
class DB_Complain {
 
    private $db;
  
    //put your code here
    // constructor
    function __construct() {
         
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
 
        $this->db->connect();
   
  }
   
  // destructor
   
  function __destruct() {
   
  }
   
  /**
   
  * Storing new user
   
  * returns user details
   
  */
   
  public function storeComplain($heading, $description, $category,$location) {
 
    $conn=mysqli_connect("localhost", "root", "golden786","android_api");
          
       
    $result = mysqli_query($conn,"INSERT INTO COMPLAIN(heading,description, category, location) VALUES('$heading', '$description', '$category', '$location')");
      
  }
   
  
   
  public function isComplainExisted($heading) {
   
    $conn=mysqli_connect("localhost", "root", "golden786","android_api");
     
    $result = mysqli_query($conn,"SELECT heading from COMPLAIN WHERE heading = '$heading'");
     
    if (!$result> 0) {
     
    // user existed
     
    return true;
   
  } else {
   
    // user not existed
     
    return false;
   
  }
 
  }
   
    
}
 
?>
