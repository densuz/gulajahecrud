<?php
require_once('koneksi.php');
header("Content-Type: application/json");
$response = array();
$access_key = "123456";


//ambil Data produk
if(isset($_POST['get_data_dbrest'])){
	if($access_key != $_POST['access_key']){
		$response['error'] = "true";
		$response['message'] = "Invalid Access Key";
		echo(json_encode($response));
		return false;
	}	
		$query = $mysqli->query('SELECT id,nama_produk,tipe_produk,harga,stok FROM produk ORDER BY id ASC LIMIT 4');
	
		while($row = $result  = $query->fetch_array(MYSQLI_ASSOC)){
			$Data_arr []= $result;			
		}		
		
		if (!empty($Data_arr)) {			
			$response['error'] = "false";
			$response['Data'] = $Data_arr;
		}else{
			$response['error'] = "true";
			$response['message'] = "No Data found!";
		}
		echo(json_encode($response));
	
}

//menambah data produk
if(isset($_POST['set_data_dbrest'])){
	if($access_key != $_POST['access_key']){
		$response['error'] = "true";
		$response['message'] = "Invalid Access Key";
		print_r(json_encode($response));
		return false;
	}
	
	$nama_produk = $mysqli->escape_string($_POST['nama_produk']);
	$tipe_produk = $mysqli->escape_string($_POST['tipe_produk']);
	$harga = $mysqli->escape_string($_POST['harga']);
	$stok = $mysqli->escape_string($_POST['stok']);
	$query = $mysqli->query("INSERT INTO data_sensor(nama_produk,tipe_produk,harga,stok) VALUES ('$nama_produk','$tipe_produk','$harga','$stok')");
	
	if($query){
		$response['error'] = "false";
		$response['message'] = "data berhasil disimpan";
	} else {
		$response['error'] = "true";
		$response['message'] = "data gagal disimpan!";
	}
	print_r(json_encode($response));
}

//edit data sensor
if(isset($_POST['edit_data_dbrest'])){
	if($access_key != $_POST['access_key']){
		$response['error'] = "true";
		$response['message'] = "Invalid Access Key";
		print_r(json_encode($response));
		return false;
	}
	$nama_produk = $mysqli->escape_string($_POST['nama_produk']);
	$tipe_produk = $mysqli->escape_string($_POST['tipe_produk']);
	$harga = $mysqli->escape_string($_POST['harga']);
	$stok = $mysqli->escape_string($_POST['stok']);
	$id=$mysqli->escape_string($_POST['id']);
	$query = $mysqli->query("UPDATE data_sensor set nama_produk='$nama_produk',tipe_produk='$tipe_produk', harga='$harga',stok='$stok' where id ='$id'");
	
	if($query){
		$response['error'] = "false";
		$response['message'] = "data berhasil diedit";
	} else {
		$response['error'] = "true";
		$response['message'] = "data gagal diedit!";
	}
	print_r(json_encode($response));
}

//Hapus data produk
if(isset($_POST['edit_data_dbrest'])){
	if($access_key != $_POST['access_key']){
		$response['error'] = "true";
		$response['message'] = "Invalid Access Key";
		print_r(json_encode($response));
		return false;
	}
	
	$id=$mysqli->escape_string($_POST['id']);
	$query = $mysqli->query("DELETE * from produk  where id ='$id'");
	
	if($query){
		$response['error'] = "false";
		$response['message'] = "data berhasil di Hapus";
	} else {
		$response['error'] = "true";
		$response['message'] = "data gagal di Hapus!";
	}
	print_r(json_encode($response));
}

?>
