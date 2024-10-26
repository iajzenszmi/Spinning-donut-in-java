public class SpinningDonut {

    public static void main(String[] args) throws InterruptedException {
        int width = 40, height = 22;
        double A = 0, B = 0;
        double[][] zBuffer = new double[height][width];
        char[][] output = new char[height][width];
        double theta_spacing = 0.07, phi_spacing = 0.02;
        double R1 = 1, R2 = 2, K2 = 5, K1 = width * K2 * 3 / (8 * (R1 + R2));

        while (true) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    zBuffer[i][j] = 0;
                    output[i][j] = ' ';
                }
            }

            for (double theta = 0; theta < 2 * Math.PI; theta += theta_spacing) {
                for (double phi = 0; phi < 2 * Math.PI; phi += phi_spacing) {
                    double cosTheta = Math.cos(theta), sinTheta = Math.sin(theta);
                    double cosPhi = Math.cos(phi), sinPhi = Math.sin(phi);
                    double circleX = R2 + R1 * cosTheta;
                    double circleY = R1 * sinTheta;

                    double x = circleX * (Math.cos(B) * cosPhi + Math.sin(A) * Math.sin(B) * sinPhi) - circleY * Math.cos(A) * Math.sin(B);
                    double y = circleX * (Math.sin(B) * cosPhi - Math.sin(A) * Math.cos(B) * sinPhi) + circleY * Math.cos(A) * Math.cos(B);
                    double z = K2 + Math.cos(A) * circleX * sinPhi + circleY * Math.sin(A);
                    double ooz = 1 / z;

                    int xp = (int) (width / 2 + K1 * ooz * x);
                    int yp = (int) (height / 2 - K1 * ooz * y);

                    if (xp >= 0 && xp < width && yp >= 0 && yp < height) {
                        double luminance = cosPhi * cosTheta * Math.sin(B) - Math.cos(A) * cosTheta * sinPhi - Math.sin(A) * sinTheta + Math.cos(B) * (Math.cos(A) * sinTheta - cosTheta * Math.sin(A) * sinPhi);
                        if (luminance > 0) {
                            zBuffer[yp][xp] = ooz;
                            int luminance_index = (int) (luminance * 8);
                            char[] chars = ".,-~:;=!*#$@".toCharArray();
                            output[yp][xp] = chars[Math.min(Math.max(luminance_index, 0), chars.length - 1)];
                        }
                    }
                }
            }

            System.out.print("\u001b[H");
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    System.out.print(output[i][j]);
                }
                System.out.println();
            }

            A += 0.04;
            B += 0.02;
            Thread.sleep(30);
        }
    }
}
