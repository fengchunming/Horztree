package com.an.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理类
 * Created by karas on 1/1/15.
 */
public class ImageUtil {
    enum YPOS {
        top, center, bottom, auto
    }

    enum XPOS {
        left, center, right, auto
    }

    /**
     * 剪切图片,没有处理图片后缀名是否正确,还有gif动态图片
     *
     * @param sourcePath 源路径(包含图片)
     * @param targetPath 目标路径 null则默认为源路径
     * @param x          起点x坐标
     * @param y          起点y左边
     * @param width      剪切宽度
     * @param height     剪切高度
     * @throws IOException if sourcePath image doesn't exist
     */
    public static void crop(String sourcePath, String targetPath, int x, int y, int width, int height) throws IOException {
        BufferedImage image = ReadImage(sourcePath);
        image = image.getSubimage(x, y, width, height);
        WriteImage(targetPath, image);
    }

    /**
     * * 将图片按照指定的图片尺寸、源图片质量压缩(默认质量为1)
     *
     * @param source :源图片路径
     * @param target :输出的压缩图片的路径
     * @param width  :压缩后的图片宽
     * @param height :压缩后的图片高
     */
    public static void zoom(String source, String target, int width, int height, boolean stretch) throws IOException {
        BufferedImage image = ReadImage(source);
        if (stretch) {
            image = stretch(image, width, height);
        } else {
            image = zoom(image, width, height);
        }
        WriteImage(target, image);
    }

    /**
     * * 将图片按照指定的图片尺寸、源图片质量压缩
     *
     * @param source :源图片路径
     * @param target :输出的压缩图片的路径
     * @param width  :压缩后的图片宽
     * @param height :压缩后的图片高
     */
    public static void fill(String source, String target, int width, int height) throws IOException {
        BufferedImage image = ReadImage(source);
        int old_w = image.getWidth(); // 得到源图宽
        int old_h = image.getHeight();  // 得到源图长
        int new_w, new_h, x = 0, y = 0;
        if ((float) width / old_w < (float) height / old_h) {
            new_w = Math.round(old_w * ((float) height / old_h));
            new_h = height;
            x = (new_w - width) / 2;
        } else {
            new_w = width;
            new_h = Math.round(old_h * ((float) width / old_w));
            y = (new_h - height) / 2;
        }
        image = zoom(image, new_w, new_h);
        image = image.getSubimage(x, y, width, height);

        WriteImage(target, image);
    }


    /**
     * 给图片添加水印、可设置水印图片旋转角度
     *
     * @param iconPath 水印图片路径
     * @param source   源图片路径
     * @param target   目标图片路径
     * @param margin   边距
     */
    public static void waterMark(String source, String target, String iconPath,
                                 YPOS y, XPOS x, Integer margin) throws IOException {

        BufferedImage buffImg = ReadImage(source);
        int w = buffImg.getWidth();
        int h = buffImg.getHeight();
        Graphics2D g = buffImg.createGraphics();  // 得到画笔对象

        // 设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.drawImage(buffImg.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);

//        if (null != degree) { // 设置水印旋转
//            g.rotate(Math.toRadians(degree), (double) w / 2, (double) h / 2);
//        }
        // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
        ImageIcon imgIcon = new ImageIcon(iconPath);

        Image img = imgIcon.getImage(); // 得到Image对象
        int iw = img.getWidth(null);
        int ih = img.getHeight(null);
        float alpha = 0.3f; // 透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        int xp, yp;
        // 表示水印图片的位置
        switch (x) {
            case left:
                xp = margin;
                break;
            case center:
                xp = (w - iw) / 2 - margin;
                break;
            case right:
                xp = w - iw - margin;
                break;
            default:
                xp = margin;
        }
        switch (y) {
            case top:
                yp = margin;
                break;
            case center:
                yp = (h - ih) / 2 - margin;
                break;
            case bottom:
                yp = h - ih - margin;
                break;
            default:
                yp = margin;
        }
        g.drawImage(img, xp, yp, null);


        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.dispose();

        WriteImage(target, buffImg);
    }


    /**
     * 将图片压缩成指定宽度， 高度等比例缩放
     *
     * @param source
     * @param maxWidth
     * @param maxHeight
     */
    private static BufferedImage zoom(BufferedImage source, int maxWidth, int maxHeight) throws IOException {
        int old_w = source.getWidth(); // 得到源图宽
        int old_h = source.getHeight();  // 得到源图长
        int new_w, new_h;
        if ((float) maxWidth / old_w < (float) maxHeight / old_h) {
            new_w = maxWidth;
            new_h = Math.round(old_h * ((float) maxWidth / old_w));
        } else {
            new_w = Math.round(old_w * ((float) maxHeight / old_h));
            new_h = maxHeight;
        }
        return stretch(source, new_w, new_h);
    }


    /**
     * 压缩图片
     *
     * @param sourceImage 待压缩图片
     * @param width       压缩图片高度
     * @param height      压缩图片宽度
     */
    private static BufferedImage stretch(BufferedImage sourceImage, int width, int height) {
        BufferedImage zoomImage = new BufferedImage(width, height, sourceImage.getType());
        Image image = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Graphics gc = zoomImage.getGraphics();
        gc.setColor(Color.WHITE);
        gc.drawImage(image, 0, 0, null);
        return zoomImage;
    }


    /**
     * * 图片文件读取
     *
     * @param sourcePath
     * @return
     */
    private static BufferedImage ReadImage(String sourcePath) throws RuntimeException, IOException {
        File imageFile = new File(sourcePath);
        if (!imageFile.exists()) {
            throw new IOException("Not found the images:" + sourcePath);
        }
        return ImageIO.read(imageFile);
    }

    private static void WriteImage(String targetPath, BufferedImage image) throws IOException {
        File file = new File(targetPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ImageIO.write(image, "jpg", file);
    }

    public static void main(String[] args) {
        try {
            ImageUtil.fill("/Users/karas/Pictures/201116030313-20224.jpg", "/Users/karas/Downloads/small.jpg", 200, 200);
            ImageUtil.waterMark("/Users/karas/Pictures/201116030313-20224.jpg", "/Users/karas/Downloads/mark.jpg", "/Users/karas/Pictures/tree56.png", YPOS.top, XPOS.left, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
