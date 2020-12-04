<?php
    $mysqli = new MySQLi("localhost", "root", "", "dbrest");;
 
    //cek koneksi		
    if (mysqli_connect_errno()){
        echo "Koneksi database mysqli gagal!!! : " . mysqli_connect_error();
    }
?>