<?php

$n=$_POST['name'];
$c=$_POST['comment'];
$servername="localhost";
$usern="root";
$conn=mysql_connect($servername,$usern,"");
if(!$conn){
	die("Connection failed: ".mysql_error());
}
mysql_select_db("ntal",$conn);

$sql="insert into ntal(`name`,`comment` )  values ('$n','$c')";
echo $sql;

if(mysql_query($sql))
	echo 1;
header('Location: asd.php');



?>