<?php
	include "connect.php";
	$query = "SELECT * FROM loaisanpham";
	$data = mysqli_query ($conn, $query);
	$mangloaisp = array();
	while ($row = mysqli_fetch_assoc($data)) {
		array_push($mangloaisp, new Loaisp(
			$row['id'],
			$row['tenLoaiSanPham'],
			$row['hinhLoaiSanPham']));
	}
	echo json_encode($mangloaisp);

	class Loaisp{
		function Loaisp($id, $tenLoaiSanPham, $hinhLoaiSanPham)
		{
			$this->id=$id;
			$this->tenLoaiSanPham = $tenLoaiSanPham;
			$this->hinhLoaiSanPham = $hinhLoaiSanPham;
		}
	}
?>