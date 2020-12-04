<?php
require_once('koneksi.php');
$myArray = array();
if ($result = mysqli_query($conn, "SELECT * FROM produk ORDER BY id ASC")) {
    while ($row = $result->fetch_array(MYSQLI_ASSOC)) {
        $myArray[] = $row;
    }
    echo json_encode($myArray);
}
mysqli_close($conn);
?>