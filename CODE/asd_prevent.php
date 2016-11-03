<?php
$conn=mysql_connect('localhost','root',"");

mysql_select_db("ntal",$conn);

$query = "SELECT * from ntal";

$res = mysql_query($query,$conn);


?>

<html>

<body style="background-color:#FFFFFF;">
<center> <h2>Nikon D5200 with (AF-S 18 - 55 mm VR II + AF-S 55 - 200 mm ED VR II Kit Lens) DSLR Camera  (Black)</h2></center>
<center> <img src="https://rukminim1.flixcart.com/image/832/832/camera/r/j/j/nikon-d5200-with-af-s-18-55-mm-vr-kit-nikon-af-s-dx-vr-zoom-original-imadte3wghzsfczs.jpeg?q=70" width="400px"/><br></center> 

<center> <h2><b>Rs. 21,999/-</b></h2>
<br>
<input type="button" value="BUY" style="background-color:Yellow;font-size:22px"></input>
</center> 

<br>
<h2>Comments</h2>
<br>
<?php

while($row = mysql_fetch_array($res, MYSQL_ASSOC)) {
      echo "<font style='font-size:20px'>\"". strip_tags($row['comment']) . "\"</font><br><br>";
	  echo " <font style='font-size:20px'>-".$row['name']."</font><br><br>";
        
   }



?>
<h2>Please enter your valuable comment</h2><br>
<form action= "put_comment_2.php" method="post">
<table><tr><td>
Name:</td>
<td><input type="text" id="name" name="name" required /></td></tr>
<tr><td>
Comment:</td>
<td><textarea rows="10" cols="40" id="comment" name="comment" required></textarea></td></tr>
</table>
<input type="submit" value="submit" style="font-size:22px"> 
</form>


</body>
</html>