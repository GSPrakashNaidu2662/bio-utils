package io.mosip.biometrics.util.test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.face.*;
import io.mosip.biometrics.util.finger.*;
import io.mosip.biometrics.util.iris.*;

/**
 * BioUtilApplication
 *
 */
public class BioUtilApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(BioUtilApplication.class);

	@SuppressWarnings({ "java:S3776", "java:S6541" })
	public static void main(String[] args) {
		if (args != null && args.length >= 2) {
			// Argument 0 should contain
			// io.mosip.biometrics.util.image.type.jp2000/io.mosip.biometrics.util.image.type.wsq"
			String imageType = args[0];
			LOGGER.info("main :: imageType :: Argument {} ", imageType);
			// 0 or 1
			if (imageType.contains(ApplicationConstant.IMAGE_TYPE_JP2000)
					|| imageType.contains(ApplicationConstant.IMAGE_TYPE_WSQ)) {
				imageType = imageType.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 1 should contain
			// "io.mosip.biometrics.util.convert.image.to.iso/io.mosip.biometrics.util.convert.iso.to.image"
			String convertTo = args[1];
			LOGGER.info("main :: convertTo :: Argument {}", convertTo);
			// 0 or 1
			if (convertTo.contains(ApplicationConstant.MOSIP_CONVERT_IMAGE_TO_ISO)
					|| convertTo.contains(ApplicationConstant.MOSIP_CONVERT_ISO_TO_IMAGE)) {
				convertTo = convertTo.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 2 should contain
			// "mosip.mock.sbi.biometric.type.finger.folder.path/mosip.mock.sbi.biometric.type.face.folder.path/mosip.mock.sbi.biometric.type.iris.folder.path"
			String biometricFolderPath = args[2];
			LOGGER.info("main :: biometricFolderPath :: Argument {} ", biometricFolderPath);
			if (biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FINGER)
					|| biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FACE)
					|| biometricFolderPath.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_IRIS)) {
				biometricFolderPath = biometricFolderPath.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 3 should contain
			// "mosip.mock.sbi.biometric.type.file.image/mosip.mock.sbi.biometric.type.file.iso"
			String converionFile = args[3];
			LOGGER.info("main :: converionFile :: Argument {} ", converionFile);
			if (converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_IMAGE)
					|| converionFile.contains(ApplicationConstant.MOSIP_BIOMETRIC_TYPE_FILE_ISO)) {
				converionFile = converionFile.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 4 should contain "mosip.mock.sbi.biometric.subtype=Right"
			// Example#Iris Right eye
			String biometricSubType = args[4];
			LOGGER.info("main :: biometricSubType :: Argument {} ", biometricSubType);
			if (biometricSubType != null && (biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_UNKNOWN)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_THUMB)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_INDEX)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_MIDDLE)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_RING)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_RIGHT_LITTLE)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_THUMB)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_INDEX)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_MIDDLE)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_RING)
					|| biometricSubType.contains(ApplicationConstant.BIO_SUB_TYPE_LEFT_LITTLE))) {
				biometricSubType = biometricSubType.split("=")[1];
			} else {
				System.exit(-1);
			}

			// Argument 5 should contain
			// "io.mosip.biometrics.util.purpose.registration=REGISTRATION/io.mosip.biometrics.util.purpose.auth=AUTH"
			String purpose = args[5];
			LOGGER.info("main :: purpose :: Argument {} ", purpose);
			if (purpose.contains(ApplicationConstant.MOSIP_PURPOSE_AUTH)
					|| purpose.contains(ApplicationConstant.MOSIP_PURPOSE_REGISTRATION)) {
				purpose = purpose.split("=")[1];
			} else {
				System.exit(-1);
			}

			if (biometricFolderPath.contains("Face")) {
				doFaceConversion(purpose, imageType, convertTo, biometricFolderPath, converionFile);
			} else if (biometricFolderPath.contains("Iris")) {
				if (biometricSubType != null) {
					doIrisConversion(purpose, imageType, convertTo, biometricFolderPath, converionFile,
							biometricSubType);
				} else {
					LOGGER.info("main :: biometricSubType :: Argument {} is empty for Iris ", biometricSubType);
				}
			} else if (biometricFolderPath.contains("Finger")) {
				if (biometricSubType != null) {
					doFingerConversion(purpose, imageType, convertTo, biometricFolderPath, converionFile,
							biometricSubType);
				} else {
					LOGGER.info("main :: biometricSubType :: Argument {}  is empty for Iris", biometricSubType);
				}
			}
		}
	}

	@SuppressWarnings({ "java:S2093", "java:S3776" })
	public static void doFaceConversion(String purpose, String inputImageType, String convertTo,
			String biometricFolderPath, String converionFile) {
		LOGGER.info(
				"doFaceConversion :: Started :: inputImageType :: {} :: convertTo :: {} :: biometricFolderPath :: {} :: converionFile :: {}",
				inputImageType, convertTo, biometricFolderPath, converionFile);
		FileOutputStream tmpOutputStream = null;
		try {
			ConvertRequestDto requestDto = new ConvertRequestDto();
			requestDto.setModality("Face");
			requestDto.setPurpose(purpose);
			requestDto.setVersion("ISO19794_5_2011");

			if (Integer.parseInt(convertTo) == 0) // Convert JP2000 to Face ISO/IEC 19794-5: 2011
			{
				String filePath = new File(".").getCanonicalPath();
				String fileName = filePath + biometricFolderPath + converionFile;
				File initialFile = new File(fileName);
				if (initialFile.exists()) {
					LOGGER.info("doFaceConversion :: fileName :: {}", fileName);

					byte[] imageData = Files.readAllBytes(Paths.get(fileName));
					if (imageData != null) {
						requestDto.setImageType(Integer.parseInt(inputImageType));
						requestDto.setInputBytes(imageData);

						byte[] isoData = FaceEncoder.convertFaceImageToISO(requestDto);
						if (isoData != null) {
							// Write bytes to tmp file.
							File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".iso");
							tmpOutputStream = new FileOutputStream(tmpImageFile);
							tmpOutputStream.write(isoData);
						} else {
							LOGGER.error("doFaceConversion :: Could Not convert the Image To ISO ");
						}
					} else {
						LOGGER.error("doFaceConversion :: Could Not convert the Image To ISO ");
					}
				}
			} else if (Integer.parseInt(convertTo) == 1) // Convert Face ISO/IEC 19794-5: 2011 to JPG
			{
				String filePath = new File(".").getCanonicalPath();
				String fileName = filePath + biometricFolderPath + converionFile;
				File initialFile = new File(fileName);
				if (initialFile.exists()) {
					LOGGER.info("doFaceConversion :: fileName :: {}", fileName);
					byte[] isoData = Files.readAllBytes(Paths.get(fileName));
					requestDto.setInputBytes(isoData);
					requestDto.setOnlyImageInformation(1);

					byte[] imageData = FaceDecoder.convertFaceISOToImageBytes(requestDto);
					if (imageData != null) {
						// Write bytes to tmp file.
						File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".jpg");
						tmpOutputStream = new FileOutputStream(tmpImageFile);
						tmpOutputStream.write(imageData);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.info("doFaceConversion :: Error ", ex);
		} finally {
			try {
				if (tmpOutputStream != null)
					tmpOutputStream.close();
			} catch (Exception ex) {
				LOGGER.info("doFaceConversion :: Error ", ex);
			}
		}
		LOGGER.info("doFaceConversion :: Ended :: ");
	}

	@SuppressWarnings({ "java:S2093", "java:S3776" })
	public static void doIrisConversion(String purpose, String inputImageType, String convertTo,
			String biometricFolderPath, String converionFile, String biometricSubType) {
		LOGGER.info(
				"doIrisConversion :: Started :: inputImageType :: {} :: convertTo :: {} :: biometricFolderPath :: {} :: converionFile :: {} :: biometricSubType {} ",
				inputImageType, convertTo, biometricFolderPath, converionFile, biometricSubType);
		FileOutputStream tmpOutputStream = null;
		try {
			ConvertRequestDto requestDto = new ConvertRequestDto();
			requestDto.setModality("Iris");
			requestDto.setPurpose(purpose);
			requestDto.setVersion("ISO19794_6_2011");

			if (Integer.parseInt(convertTo) == 0) // Convert JP2000 to IRIS ISO/IEC 19794-6: 2011
			{
				String filePath = new File(".").getCanonicalPath();
				String fileName = filePath + biometricFolderPath + converionFile;
				File initialFile = new File(fileName);
				if (initialFile.exists()) {
					LOGGER.info("doIrisConversion :: fileName :: {}", fileName);
					byte[] imageData = Files.readAllBytes(Paths.get(fileName));
					if (imageData != null) {
						requestDto.setImageType(Integer.parseInt(inputImageType));
						requestDto.setBiometricSubType(biometricSubType);
						requestDto.setInputBytes(imageData);

						byte[] isoData = IrisEncoder.convertIrisImageToISO(requestDto);
						if (isoData != null) {
							// Write bytes to tmp file.
							File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".iso");
							tmpOutputStream = new FileOutputStream(tmpImageFile);
							tmpOutputStream.write(isoData);
						}
					} else {
						LOGGER.error("doIrisConversion :: Could Not convert the Image To ISO ");
					}
				}
			} else if (Integer.parseInt(convertTo) == 1) // Convert IRIS ISO/IEC 19794-6: 2011 to JPG
			{
				String filePath = new File(".").getCanonicalPath();
				String fileName = filePath + biometricFolderPath + converionFile;
				File initialFile = new File(fileName);
				if (initialFile.exists()) {
					LOGGER.info("doIrisConversion :: fileName ::{}", fileName);
					byte[] isoData = Files.readAllBytes(Paths.get(fileName));
					requestDto.setInputBytes(isoData);
					requestDto.setOnlyImageInformation(1);
					byte[] imageData = IrisDecoder.convertIrisISOToImageBytes(requestDto);

					if (imageData != null) {
						// Write bytes to tmp file.
						File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".jpg");
						tmpOutputStream = new FileOutputStream(tmpImageFile);
						tmpOutputStream.write(imageData);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.info("doIrisConversion :: Error ", ex);
		} finally {
			try {
				if (tmpOutputStream != null)
					tmpOutputStream.close();
			} catch (Exception ex) {
				LOGGER.info("doIrisConversion :: Error ", ex);
			}
		}
		LOGGER.info("doIrisConversion :: Ended :: ");
	}

	@SuppressWarnings({ "java:S2093", "java:S3776" })
	public static void doFingerConversion(String purpose, String inputImageType, String convertTo,
			String biometricFolderPath, String converionFile, String biometricSubType) {
		LOGGER.info(
				"doFingerConversion :: Started :: inputImageType :: {} :: convertTo :: {} :: biometricFolderPath :: {} :: converionFile :: {} :: biometricSubType {} ",
				inputImageType, convertTo, biometricFolderPath, converionFile, biometricSubType);

		FileOutputStream tmpOutputStream = null;
		try {
			ConvertRequestDto requestDto = new ConvertRequestDto();
			requestDto.setModality("Finger");
			requestDto.setPurpose(purpose);
			requestDto.setVersion("ISO19794_4_2011");

			if (Integer.parseInt(convertTo) == 0) // Convert JP2000/WSQ to Finger ISO/IEC 19794-4: 2011
			{
				String filePath = new File(".").getCanonicalPath();
				String fileName = filePath + biometricFolderPath + converionFile;
				File initialFile = new File(fileName);
				if (initialFile.exists()) {
					LOGGER.info("doFingerConversion :: fileName :: {}", fileName);

					byte[] imageData = Files.readAllBytes(Paths.get(fileName));
					if (imageData != null) {
						requestDto.setImageType(Integer.parseInt(inputImageType));
						requestDto.setBiometricSubType(biometricSubType);
						requestDto.setInputBytes(imageData);

						byte[] isoData = FingerEncoder.convertFingerImageToISO(requestDto);
						if (isoData != null) {
							// Write bytes to tmp file.
							File tmpImageFile = new File(filePath + biometricFolderPath + converionFile + ".iso");
							tmpOutputStream = new FileOutputStream(tmpImageFile);
							tmpOutputStream.write(isoData);
						}
					} else {
						LOGGER.error("doFingerConversion :: Could Not convert the Image To ISO ");
					}
				}
			} else if (Integer.parseInt(convertTo) == 1) // Convert Finger ISO/IEC 19794-4: 2011 to JPG/WSQ
			{
				String filePath = new File(".").getCanonicalPath();
				String fileName = filePath + biometricFolderPath + converionFile;
				File initialFile = new File(fileName);
				if (initialFile.exists()) {
					LOGGER.info("doFingerConversion :: fileName ::{}", fileName);
					byte[] isoData = Files.readAllBytes(Paths.get(fileName));
					requestDto.setInputBytes(isoData);
					requestDto.setOnlyImageInformation(1);

					byte[] imageData = FingerDecoder.convertFingerISOToImageBytes(requestDto);
					if (imageData != null) {
						// Write bytes to tmp file.
						if (Integer.parseInt(inputImageType) == 0) // ApplicationConstant.IMAGE_TYPE_JP2000))
							fileName = filePath + biometricFolderPath + converionFile + ".jpg";
						else if (Integer.parseInt(inputImageType) == 1) // ApplicationConstant.IMAGE_TYPE_WSQ))
							fileName = filePath + biometricFolderPath + converionFile + ".wsq";
						File tmpImageFile = new File(fileName);
						tmpOutputStream = new FileOutputStream(tmpImageFile);
						tmpOutputStream.write(imageData);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.info("doFingerConversion :: Error ", ex);
		} finally {
			try {
				if (tmpOutputStream != null)
					tmpOutputStream.close();
			} catch (Exception ex) {
				LOGGER.info("doFingerConversion :: Error ", ex);
			}
		}
		LOGGER.info("doFingerConversion :: Ended :: ");
	}
}