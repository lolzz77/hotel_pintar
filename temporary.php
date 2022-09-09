<?php
//Change name here for conveniences.
$tableName = 'deluxeTripleRoom';
$proceed = TRUE;

$server = mysqli_connect("localhost","root","","hotel_pintar");

$today = date("Ymd"); //Y=Year,m=month,d=day. Y=4 digits, y = 2 digits.
$temp = $today;
$maxDate = date("Ymd", strtotime("+5 month"));

while ($today <= $maxDate) {
  $temp = 'D' . $today; // the dot, is called 'to append', similar to java 'Hello' + ' World', I have to append a letter in front, if not it wont create the table in database.

  //If table exists, the CREATE TABLE will be ignored. Use ALTER TABLE then
  $sql = "CREATE TABLE $tableName
  ($temp TINYINT(2) UNSIGNED NOT NULL)";

  //If table does not exists, adding these 2 codes together will not work.
  //First create table, then only you can run these 2 codes together will it be functional
  //If table haven't create, make it FALSE, run 1 time. Then make it TRUE, and run 2nd time.
  if($proceed) {
    $sql = "ALTER TABLE $tableName
    ADD
    ($temp TINYINT(2) UNSIGNED NOT NULL)";
  }

  //This function execute SQL code
  mysqli_query($server, $sql);

  //To increment date by 1 day in loop
  $swap = strtotime("+1 day",strtotime($today));
  $today = date('Ymd',$swap);
}

//Then, I suggest you manually add value by clicking 'INSERT' in phpMyAdmin.
//Rmb go down at the page, and make sure 'Continue Insertion with 1 row', by default it insert 2 rows.
//Use Tab to insert it quickly
//Dont use code, it's complicated

mysqli_close($server)
?>
