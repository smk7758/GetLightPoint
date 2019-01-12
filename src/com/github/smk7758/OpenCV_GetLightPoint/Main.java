package com.github.smk7758.OpenCV_GetLightPoint;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import com.github.smk7758.OpenCV_GetLightPoint.VideoCaptureFileBuilder.VideoCaptureCreater;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		new Main().processer();
	}

	public void processer() {
		VideoCaptureFileBuilder vcb = new VideoCaptureFileBuilder(
				"S:\\old_program\\2019-01-11_GetLightPoint\\IMG_1083.MP4");
		VideoCapture vc = VideoCaptureCreater.generateVideoCapture(vcb);
		VideoWriter vw = new VideoWriter("S:\\old_program\\2019-01-11_GetLightPoint\\0003.MP4", 32,
				vc.get(Videoio.CV_CAP_PROP_FPS),
				new Size(vc.get(Videoio.CAP_PROP_FRAME_WIDTH), vc.get(Videoio.CAP_PROP_FRAME_HEIGHT)));

		Mat mat = new Mat();
		Mat gray = new Mat();
		Mat light = new Mat();

		Mat result = new Mat();
		vc.read(result);
		result.convertTo(result, CvType.CV_32SC3);

		while (vc.read(mat)) {
			Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);

			Imgproc.threshold(gray, gray, 125, 255, Imgproc.THRESH_BINARY); // 光の部分を取得 => gray

			light = exceptPrevention(mat, gray);

			Imgproc.accumulateWeighted(light, result, 0.5); // 軌跡を残すようにするもの

			vw.write(result);
		}

		vc.release();
		vw.release();
		System.out.println("FIN");
	}

	public Mat exceptPrevention(Mat matBgr, Mat matPrevention) {
		Mat result = matBgr.clone();

		final Scalar replacePixelScalar = new Scalar(0, 0, 0);
		for (int h = 0; h < result.size().height; h++) {
			for (int w = 0; w < result.size().width; w++) {
				if (!isLightPoint(matPrevention, h, w)) {
					replacePixel(result, h, w, replacePixelScalar); // 光影響範囲の除外
				}
			}
		}
		return result;
	}

	public boolean isLightPoint(Mat mat_tmp_light_prevention, int h, int w) {
		if (mat_tmp_light_prevention.get(h, w)[0] > 0) return true;
		else return false;
	}

	public void replacePixel(Mat mat_light_result, int h, int w, Scalar scalar) {
		double[] val = { 0, 0, 0 };
		mat_light_result.put(h, w, val); // 光影響範囲の除外
	}
}
