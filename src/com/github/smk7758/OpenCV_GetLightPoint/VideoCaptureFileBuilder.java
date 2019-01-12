package com.github.smk7758.OpenCV_GetLightPoint;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.opencv.videoio.VideoCapture;

public class VideoCaptureFileBuilder {
	Path path = null;

	public VideoCaptureFileBuilder(Path path) {
		this.path = path;
	}

	public VideoCaptureFileBuilder(String path) {
		this.path = Paths.get(path);
	}

	public Path getPath() {
		return path;
	}

	public static class VideoCaptureCreater {
		private VideoCaptureCreater() {
		}

		public static VideoCapture generateVideoCapture(VideoCaptureFileBuilder vcb) {
			VideoCapture vc = new VideoCapture();
			vc.open(vcb.path.toString());
			return vc;
		}
	}
}
