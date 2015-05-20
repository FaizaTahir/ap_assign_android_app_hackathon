<?php
  
class DB_Functions {
 
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
   
  public function storeUser($name, $email, $password) {
 
    $conn=mysqli_connect("localhost", "root", "golden786","android_api");
     
    $uuid = uniqid('', true);
     
    $hash = $this->hashSSHA($password);
     
    $encrypted_password = $hash["encrypted"]; // encrypted password
     
    $salt = $hash["salt"]; // salt
     
    $result = mysqli_query($conn,"INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())");
     
    // check for successful store
     
    if ($result) {
     
      // get user details
       
      $uid = mysqli_insert_id(); // last inserted id
       
      $result = mysqli_query($conn,"SELECT * FROM users WHERE uid = $uid");
       
      // return user details
       
      return mysqli_fetch_array($result);
     
    } else {
 
      return false;
     
    }
 
  }
   
  /**
   
  * Get user by email and password
   
  */
   
  public function getUserByEmailAndPassword($email, $password) {
   
    $conn=mysqli_connect("localhost", "root", "golden786","android_api");
     
    $result = mysqli_query($conn,"SELECT * FROM users WHERE email = '$email'");
     
    // check for result
     
    $no_of_rows = mysqli_num_rows($result);
     
    if ($no_of_rows > 0) {
     
    $result = mysqli_fetch_array($result);
     
    $salt = $result['salt'];
     
    $encrypted_password = $result['encrypted_password'];
     
    $hash = $this->checkhashSSHA($salt, $password);
     
    // check for password equality
   
      if ($encrypted_password == $hash) {
     
        // user authentication details are correct
         
        return $result;
       
      }
 
  } else {
   
  // user not found
   
     return false;
   
  }
   
  }
   
  /**
   
  * Check user is existed or not
   
  */
   
  public function isUserExisted($email) {
   
    $conn=mysqli_connect("localhost", "root", "golden786","android_api");
     
    $result = mysqli_query($conn,"SELECT email from users WHERE email = '$email'");
     
    if (!$result||mysqli_num_rows($result)> 0) {
     
    // user existed
     
    return true;
   
  } else {
   
    // user not existed
     
    return false;
   
  }
 
  }
   
  /**
   
  * Encrypting password
   
  * @param password
   
  * returns salt and encrypted password
   
  */
   
  public function hashSSHA($password) {
 
    $salt = sha1(rand());
     
    $salt = substr($salt, 0, 10);
     
    $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
     
    $hash = array("salt" => $salt, "encrypted" => $encrypted);
     
    return $hash;
   
  }
 
  /**
   
  * Decrypting password
   
  * @param salt, password
   
  * returns hash string
   
  */
   
  public function checkhashSSHA($salt, $password) {
   
    $hash = base64_encode(sha1($password . $salt, true) . $salt);
     
    return $hash;
   
  }
 
}
 
?>