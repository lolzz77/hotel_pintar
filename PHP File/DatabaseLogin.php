<?php

include 'DatabaseConfig.php' ;

 $con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);

 $name = $_POST['name'];
 $email = $_POST['email'];

 $Sql_Query = "insert into GetDataTable (name,email) values ('$name','$email')";

 if(mysqli_query($con,$Sql_Query)){

 echo 'Data Submit Successfully';

 }
 else{

 echo 'Try Again';

 }
 mysqli_close($con);
?>
