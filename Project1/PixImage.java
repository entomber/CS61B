/* PixImage.java */

/**
 *  The PixImage class represents an image, which is a rectangular grid of
 *  color pixels.  Each pixel has red, green, and blue intensities in the range
 *  0...255.  Descriptions of the methods you must implement appear below.
 *  They include a constructor of the form
 *
 *      public PixImage(int width, int height);
 *
 *  that creates a black (zero intensity) image of the specified width and
 *  height.  Pixels are numbered in the range (0...width - 1, 0...height - 1).
 *
 *  All methods in this class must be implemented to complete Part I.
 *  See the README file accompanying this project for additional details.
 */

public class PixImage {

  /**
   *  Define any variables associated with a PixImage object here.  These
   *  variables MUST be private.
   */
  private short[][][] image;
  private final int width;
  private final int height;


  /**
   * PixImage() constructs an empty PixImage with a specified width and height.
   * Every pixel has red, green, and blue intensities of zero (solid black).
   *
   * @param width the width of the image.
   * @param height the height of the image.
   */
  public PixImage(int width, int height) {
    // Your solution here.
    this.width = width;
    this.height = height;
    image = new short[width][height][3];
  }

  /**
   * getWidth() returns the width of the image.
   *
   * @return the width of the image.
   */
  public int getWidth() {
    // Replace the following line with your solution.
    return width;
  }

  /**
   * getHeight() returns the height of the image.
   *
   * @return the height of the image.
   */
  public int getHeight() {
    // Replace the following line with your solution.
    return height;
  }

  /**
   * getRed() returns the red intensity of the pixel at coordinate (x, y).
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @return the red intensity of the pixel at coordinate (x, y).
   */
  public short getRed(int x, int y) {
    // Replace the following line with your solution.
    return image[x][y][0];
  }

  /**
   * getGreen() returns the green intensity of the pixel at coordinate (x, y).
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @return the green intensity of the pixel at coordinate (x, y).
   */
  public short getGreen(int x, int y) {
    // Replace the following line with your solution.
    return image[x][y][1];
  }

  /**
   * getBlue() returns the blue intensity of the pixel at coordinate (x, y).
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @return the blue intensity of the pixel at coordinate (x, y).
   */
  public short getBlue(int x, int y) {
    // Replace the following line with your solution.
    return image[x][y][2];
  }

  /**
   * setPixel() sets the pixel at coordinate (x, y) to specified red, green,
   * and blue intensities.
   *
   * If any of the three color intensities is NOT in the range 0...255, then
   * this method does NOT change any of the pixel intensities.
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @param red the new red intensity for the pixel at coordinate (x, y).
   * @param green the new green intensity for the pixel at coordinate (x, y).
   * @param blue the new blue intensity for the pixel at coordinate (x, y).
   */
  public void setPixel(int x, int y, short red, short green, short blue) {
    // Your solution here.
    if (x >= 0 && x <= width && y >= 0 && y <= width &&
        red >= 0    && red <= 255 &&
        green >= 0  && green <= 255 &&
        blue >= 0   && blue <= 255) {
      image[x][y][0] = red;
      image[x][y][1] = green;
      image[x][y][2] = blue;
    }
  }

  /**
   * toString() returns a String representation of this PixImage.
   *
   * This method isn't required, but it should be very useful to you when
   * you're debugging your code.  It's up to you how you represent a PixImage
   * as a String.
   *
   * @return a String representation of this PixImage.
   */
  public String toString() {
    // Replace the following line with your solution.
    return "";
  }

  /**
   * boxBlur() returns a blurred version of "this" PixImage.
   *
   * If numIterations == 1, each pixel in the output PixImage is assigned
   * a value equal to the average of its neighboring pixels in "this" PixImage,
   * INCLUDING the pixel itself.
   *
   * A pixel not on the image boundary has nine neighbors--the pixel itself and
   * the eight pixels surrounding it.  A pixel on the boundary has six
   * neighbors if it is not a corner pixel; only four neighbors if it is
   * a corner pixel.  The average of the neighbors is the sum of all the
   * neighbor pixel values (including the pixel itself) divided by the number
   * of neighbors, with non-integer quotients rounded toward zero (as Java does
   * naturally when you divide two integers).
   *
   * Each color (red, green, blue) is blurred separately.  The red input should
   * have NO effect on the green or blue outputs, etc.
   *
   * The parameter numIterations specifies a number of repeated iterations of
   * box blurring to perform.  If numIterations is zero or negative, "this"
   * PixImage is returned (not a copy).  If numIterations is positive, the
   * return value is a newly constructed PixImage.
   *
   * IMPORTANT:  DO NOT CHANGE "this" PixImage!!!  All blurring/changes should
   * appear in the new, output PixImage only.
   *
   * @param numIterations the number of iterations of box blurring.
   * @return a blurred version of "this" PixImage.
   */
  public PixImage boxBlur(int numIterations) {
    // Replace the following line with your solution.
    if (numIterations <= 0) {
      return this;
    }
    PixImage blurred = new PixImage(width, height);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        short newRed = 0;
        short newGreen = 0;
        short newBlue = 0;

        // TODO: 1. use x and y offset variables to reduce code
        // 2. check if corner, border, or interior at beginning to reduce code

        if (x == 0 && y == 0) { 
        // top-left corner
          newRed +=   getRed(x,y)   + getRed(x+1,y) + 
                      getRed(x,y+1) + getRed(x+1,y+1);
          newGreen += getGreen(x,y)   + getGreen(x+1,y) + 
                      getGreen(x,y+1) + getGreen(x+1,y+1);
          newBlue +=  getBlue(x,y)    + getBlue(x+1,y) +
                      getBlue(x,y+1)  + getBlue(x+1,y+1);
          newRed /= 4;
          newGreen /= 4;
          newBlue /= 4;            
        } else if (x == 0 && y == height-1) { 
        // bottom-left corner
          newRed +=   getRed(x,y-1) + getRed(x+1,y-1) +
                      getRed(x,y)   + getRed(x+1,y);
          newGreen += getGreen(x,y-1) + getGreen(x+1,y-1) +
                      getGreen(x,y)   + getGreen(x+1,y);
          newBlue +=  getBlue(x,y-1) + getBlue(x+1,y-1) +
                      getBlue(x,y)   + getBlue(x+1,y);
          newRed /= 4;
          newGreen /= 4;
          newBlue /= 4;
        } else if (x == width-1 && y == 0) { 
        // top-right corner
          newRed +=   getRed(x-1,y)   + getRed(x,y) + 
                      getRed(x-1,y+1) + getRed(x,y+1);
          newGreen += getGreen(x-1,y)   + getGreen(x,y) + 
                      getGreen(x-1,y+1) + getGreen(x,y+1);
          newBlue +=  getBlue(x-1,y)    + getBlue(x,y) +
                      getBlue(x-1,y+1)  + getBlue(x,y+1);
          newRed /= 4;
          newGreen /= 4;
          newBlue /= 4;            
        } else if (x == width-1 && y == height-1) { 
        // bottom-right corner
          newRed +=   getRed(x-1,y-1) + getRed(x,y-1) +
                      getRed(x-1,y)   + getRed(x,y);
          newGreen += getGreen(x-1,y-1) + getGreen(x,y-1) +
                      getGreen(x-1,y)   + getGreen(x,y);
          newBlue +=  getBlue(x-1,y-1) + getBlue(x,y-1) +
                      getBlue(x-1,y)   + getBlue(x,y);
          newRed /= 4;
          newGreen /= 4;
          newBlue /= 4;
        } else if (x == 0) { 
        // left border
          newRed +=   getRed(x,y-1) + getRed(x+1,y-1) +
                      getRed(x,y)   + getRed(x+1,y)   +
                      getRed(x,y+1) + getRed(x+1,y+1);
          newGreen += getGreen(x,y-1) + getGreen(x+1,y-1) +
                      getGreen(x,y)   + getGreen(x+1,y)   +
                      getGreen(x,y+1) + getGreen(x+1,y+1);
          newBlue +=  getBlue(x,y-1) + getBlue(x+1,y-1) +
                      getBlue(x,y)   + getBlue(x+1,y)   +
                      getBlue(x,y+1) + getBlue(x+1,y+1);
          newRed /= 6;
          newGreen /= 6;
          newBlue /= 6;
        } else if (x == width-1) { 
        // right border
          newRed +=   getRed(x-1,y-1) + getRed(x,y-1) +
                      getRed(x-1,y)   + getRed(x,y)   +
                      getRed(x-1,y+1) + getRed(x,y+1);
          newGreen += getGreen(x-1,y-1) + getGreen(x,y-1) +
                      getGreen(x-1,y)   + getGreen(x,y)   +
                      getGreen(x-1,y+1) + getGreen(x,y+1);
          newBlue +=  getBlue(x-1,y-1) + getBlue(x,y-1) +
                      getBlue(x-1,y)   + getBlue(x,y)   +
                      getBlue(x-1,y+1) + getBlue(x,y+1);
          newRed /= 6;
          newGreen /= 6;
          newBlue /= 6;
        } else if (y == 0) { 
        // top border
          newRed +=   getRed(x-1,y)   + getRed(x,y)   + getRed(x+1,y) +
                      getRed(x-1,y+1) + getRed(x,y+1) + getRed(x+1,y+1);
          newGreen += getGreen(x-1,y)   + getGreen(x,y)   + getGreen(x+1,y) +
                      getGreen(x-1,y+1) + getGreen(x,y+1) + getGreen(x+1,y+1);
          newBlue +=  getBlue(x-1,y)    + getBlue(x,y)    + getBlue(x+1,y) +
                      getBlue(x-1,y+1)  + getBlue(x,y+1)  + getBlue(x+1,y+1);
          newRed /= 6;
          newGreen /= 6;
          newBlue /= 6;
        } else if (y == height-1) { 
        // bottom border
          newRed +=   getRed(x-1,y-1) + getRed(x,y-1) + getRed(x+1,y-1) +
                      getRed(x-1,y)   + getRed(x,y)   + getRed(x+1,y);
          newGreen += getGreen(x-1,y-1) + getGreen(x,y-1) + getGreen(x+1,y-1) +
                      getGreen(x-1,y)   + getGreen(x,y)   + getGreen(x+1,y);
          newBlue +=  getBlue(x-1,y-1) + getBlue(x,y-1) + getBlue(x+1,y-1) +
                      getBlue(x-1,y)   + getBlue(x,y)   + getBlue(x+1,y);
          newRed /= 6;
          newGreen /= 6;
          newBlue /= 6;
        } else {  
        // non-border
          newRed +=   getRed(x-1,y-1) + getRed(x,y-1) + getRed(x+1,y-1) +
                      getRed(x-1,y)   + getRed(x,y)   + getRed(x+1,y)   + 
                      getRed(x-1,y+1) + getRed(x,y+1) + getRed(x+1,y+1);
          newGreen += getGreen(x-1,y-1) + getGreen(x,y-1) + getGreen(x+1,y-1) +
                      getGreen(x-1,y)   + getGreen(x,y)   + getGreen(x+1,y)   +
                      getGreen(x-1,y+1) + getGreen(x,y+1) + getGreen(x+1,y+1);
          newBlue +=  getBlue(x-1,y-1) + getBlue(x,y-1) + getBlue(x+1,y-1) +
                      getBlue(x-1,y)   + getBlue(x,y)   + getBlue(x+1,y)   +
                      getBlue(x-1,y+1) + getBlue(x,y+1) + getBlue(x+1,y+1);
          newRed /= 9;
          newGreen /= 9;
          newBlue /= 9;
        }
        blurred.setPixel(x, y, newRed, newGreen, newBlue);
      }
    }
    numIterations--;
    return blurred.boxBlur(numIterations);
  }




  /**
   * mag2gray() maps an energy (squared vector magnitude) in the range
   * 0...24,969,600 to a grayscale intensity in the range 0...255.  The map
   * is logarithmic, but shifted so that values of 5,080 and below map to zero.
   *
   * DO NOT CHANGE THIS METHOD.  If you do, you will not be able to get the
   * correct images and pass the autograder.
   *
   * @param mag the energy (squared vector magnitude) of the pixel whose
   * intensity we want to compute.
   * @return the intensity of the output pixel.
   */
  private static short mag2gray(long mag) {
    short intensity = (short) (30.0 * Math.log(1.0 + (double) mag) - 256.0);

    // Make sure the returned intensity is in the range 0...255, regardless of
    // the input value.
    if (intensity < 0) {
      intensity = 0;
    } else if (intensity > 255) {
      intensity = 255;
    }
    return intensity;
  }

  /**
   * sobelEdges() applies the Sobel operator, identifying edges in "this"
   * image.  The Sobel operator computes a magnitude that represents how
   * strong the edge is.  We compute separate gradients for the red, blue, and
   * green components at each pixel, then sum the squares of the three
   * gradients at each pixel.  We convert the squared magnitude at each pixel
   * into a grayscale pixel intensity in the range 0...255 with the logarithmic
   * mapping encoded in mag2gray().  The output is a grayscale PixImage whose
   * pixel intensities reflect the strength of the edges.
   *
   * See http://en.wikipedia.org/wiki/Sobel_operator#Formulation for details.
   *
   * @return a grayscale PixImage representing the edges of the input image.
   * Whiter pixels represent stronger edges.
   */
  public PixImage sobelEdges() {
    // Replace the following line with your solution.

    short[][] xkernel = { {1, 0, -1}, {2, 0, -2}, {1, 0, -1} };
    short[][] ykernel = { {1, 2, 1}, {0, 0, 0}, {-1, -2, -1} };
    // gradients gx and gy for each color
    short gxRed = 0;
    short gxGreen = 0;
    short gxBlue = 0;
    short gyRed = 0;
    short gyGreen = 0;
    short gyBlue = 0;
    int energy = 0;   // energy of a pixel (sum of RGB gradients squared)
    short gray = 0;   // pixel energy converted to grayscale intensity
    PixImage edges = new PixImage(width, height);
    /* For each point, do:
        1. get surrounding pixels
        2. calculate gx and gy for each color (6 total gradients)
        3. compute energy exactly (cast as long/int before) */
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        short[][][] ref = reflect(x, y);
        gxRed   = calculateGradient(xkernel, ref, 0);
        gxGreen = calculateGradient(xkernel, ref, 1);
        gxBlue  = calculateGradient(xkernel, ref, 2);
        gyRed   = calculateGradient(ykernel, ref, 0);
        gyGreen = calculateGradient(ykernel, ref, 1);
        gyBlue  = calculateGradient(ykernel, ref, 2);
        energy =  (int) gxRed * (int) gxRed + (int) gyRed * (int) gyRed + 
                  (int) gxGreen * (int) gxGreen + (int) gyGreen * (int) gyGreen + 
                  (int) gxBlue * (int) gxBlue + (int) gyBlue* (int) gyBlue;
        gray = mag2gray(energy);
        edges.setPixel(x, y, gray, gray, gray);
      }
    }
    return edges;
  }

  // returns surrounding pixels around given x, y. reflects border pixels
  private short[][][] reflect(int x, int y) {
    short a[][][] = new short[3][3][3];
    int i = 0;
    int j = 0;
    int iStop = 2;
    int jStop = 2;

    // set start point for i, and stop points for i and i
    if (x == 0) {
      i = 1;
    } else if (x == width-1) {
      iStop = 1;
    }
    if (y == height-1) {
      jStop = 1;
    }

    // copies non-border pixels surrounding x,y
    for ( ; i <= iStop; i++) {
      if (y == 0) {
        j = 1;
      } else {
        j = 0;
      }
      for ( ; j <= jStop; j++) {
        a[i][j] = image[x-1+i][y-1+j];
      }
    }

    // reflects pixel across border
    if (x == 0 && y == 0) {
    // top-left corner
      a[0][0] = image[x][y];
      a[1][0] = image[x][y];
      a[0][1] = image[x][y];
      a[2][0] = image[x+1][y];
      a[0][2] = image[x][y+1];
    } else if (x == 0 && y == height-1) {
    // bottom-left corner
      a[0][2] = image[x][y];
      a[0][1] = image[x][y];
      a[1][2] = image[x][y];
      a[0][0] = image[x][y-1];
      a[2][2] = image[x+1][y];
    } else if (x == width-1 && y == 0) {
    // top-right corner
      a[2][0] = image[x][y];
      a[1][0] = image[x][y];
      a[2][1] = image[x][y];
      a[0][0] = image[x-1][y];
      a[2][2] = image[x][y+1];
    } else if (x == width-1 && y == height-1) {
    // bottom-right corner
      a[2][2] = image[x][y];
      a[1][2] = image[x][y];
      a[2][1] = image[x][y];
      a[0][2] = image[x-1][y];
      a[2][0] = image[x][y-1];
    } else if (x == 0) {
    // left border
      a[0][0] = image[x][y-1];
      a[0][1] = image[x][y];
      a[0][2] = image[x][y+1];
    } else if (x == width-1) {
    // right border
      a[2][0] = image[x][y-1];
      a[2][1] = image[x][y];
      a[2][2] = image[x][y+1];
    } else if (y == 0) {
    // top border
      a[0][0] = image[x-1][y];
      a[1][0] = image[x][y];
      a[2][0] = image[x+1][y];
    } else if (y == height-1) {
    // bottom border
      a[0][2] = image[x-1][y];
      a[1][2] = image[x][y];
      a[2][2] = image[x+1][y];
    }
    return a;
  }

  // calculates the gradient given:
  // 1. the kernel, 2. 3x3 neighbors array, and 3. color (0 = r, 1 = g, 2 = b)
  private short calculateGradient(short[][] kernel, short[][][] neighbors, int color) {
    short gradient = 0;
    int size = kernel.length;
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        gradient += kernel[size-1-x][size-1-y] * neighbors[x][y][color];
      }
    }
    return gradient;
  }

  /**
   * TEST CODE:  YOU DO NOT NEED TO FILL IN ANY METHODS BELOW THIS POINT.
   * You are welcome to add tests, though.  Methods below this point will not
   * be tested.  This is not the autograder, which will be provided separately.
   */


  /**
   * doTest() checks whether the condition is true and prints the given error
   * message if it is not.
   *
   * @param b the condition to check.
   * @param msg the error message to print if the condition is false.
   */
  private static void doTest(boolean b, String msg) {
    if (b) {
      System.out.println("Good.");
    } else {
      System.err.println(msg);
    }
  }

  /**
   * array2PixImage() converts a 2D array of grayscale intensities to
   * a grayscale PixImage.
   *
   * @param pixels a 2D array of grayscale intensities in the range 0...255.
   * @return a new PixImage whose red, green, and blue values are equal to
   * the input grayscale intensities.
   */
  private static PixImage array2PixImage(int[][] pixels) {
    int width = pixels.length;
    int height = pixels[0].length;
    PixImage image = new PixImage(width, height);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setPixel(x, y, (short) pixels[x][y], (short) pixels[x][y],
                       (short) pixels[x][y]);
      }
    }

    return image;
  }

  /**
   * equals() checks whether two images are the same, i.e. have the same
   * dimensions and pixels.
   *
   * @param image a PixImage to compare with "this" PixImage.
   * @return true if the specified PixImage is identical to "this" PixImage.
   */
  public boolean equals(PixImage image) {
    int width = getWidth();
    int height = getHeight();

    if (image == null ||
        width != image.getWidth() || height != image.getHeight()) {
      return false;
    }

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (! (getRed(x, y) == image.getRed(x, y) &&
               getGreen(x, y) == image.getGreen(x, y) &&
               getBlue(x, y) == image.getBlue(x, y))) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * main() runs a series of tests to ensure that the convolutions (box blur
   * and Sobel) are correct.
   */
  public static void main(String[] args) {
    // Be forwarned that when you write arrays directly in Java as below,
    // each "row" of text is a column of your image--the numbers get
    // transposed.
    PixImage image1 = array2PixImage(new int[][] { { 0, 10, 240 },
                                                   { 30, 120, 250 },
                                                   { 80, 250, 255 } });
    System.out.println("Testing getWidth/getHeight on a 3x3 image.  " +
                       "Input image:");
    System.out.print(image1);
    doTest(image1.getWidth() == 3 && image1.getHeight() == 3,
           "Incorrect image width and height.");

    System.out.println("Testing blurring on a 3x3 image.");
    doTest(image1.boxBlur(1).equals(
           array2PixImage(new int[][] { { 40, 108, 155 },
                                        { 81, 137, 187 },
                                        { 120, 164, 218 } })),
           "Incorrect box blur (1 rep):\n" + image1.boxBlur(1));
    doTest(image1.boxBlur(2).equals(
           array2PixImage(new int[][] { { 91, 118, 146 },
                                        { 108, 134, 161 },
                                        { 125, 151, 176 } })),
           "Incorrect box blur (2 rep):\n" + image1.boxBlur(2));
    doTest(image1.boxBlur(2).equals(image1.boxBlur(1).boxBlur(1)),
           "Incorrect box blur (1 rep + 1 rep):\n" +
           image1.boxBlur(2) + image1.boxBlur(1).boxBlur(1));

    System.out.println("Testing edge detection on a 3x3 image.");
    doTest(image1.sobelEdges().equals(
           array2PixImage(new int[][] { { 104, 189, 180 },
                                        { 160, 193, 157 },
                                        { 166, 178, 96 } })),
           "Incorrect Sobel:\n" + image1.sobelEdges());


    PixImage image2 = array2PixImage(new int[][] { { 0, 100, 100 },
                                                   { 0, 0, 100 } });
    System.out.println("Testing getWidth/getHeight on a 2x3 image.  " +
                       "Input image:");
    System.out.print(image2);
    doTest(image2.getWidth() == 2 && image2.getHeight() == 3,
           "Incorrect image width and height.");

    System.out.println("Testing blurring on a 2x3 image.");
    doTest(image2.boxBlur(1).equals(
           array2PixImage(new int[][] { { 25, 50, 75 },
                                        { 25, 50, 75 } })),
           "Incorrect box blur (1 rep):\n" + image2.boxBlur(1));

    System.out.println("Testing edge detection on a 2x3 image.");
    doTest(image2.sobelEdges().equals(
           array2PixImage(new int[][] { { 122, 143, 74 },
                                        { 74, 143, 122 } })),
           "Incorrect Sobel:\n" + image2.sobelEdges());

    // additional test cases
    // testBoxBlur();
    // testReflect();
  }

  private static void testBoxBlur() {
    PixImage image1 = array2PixImage(new int[][] { { 0, 10, 240 },
                                                   { 30, 120, 250 },
                                                   { 80, 250, 255 } });
    System.out.println("Testing blurring on a 3x3 image.");
    doTest(image1.boxBlur(0).equals(image1),
           "Incorrect box blur (0 rep):\n" + image1.boxBlur(0));
  }

  private static void testReflect() {
    PixImage image = array2PixImage(new int[][] { { 0, 10, 240 },
                                                  { 30, 120, 250 },
                                                  { 80, 250, 255 } });
    System.out.println("Testing reflect on a 3x3 image.");
    // prints the intensity of grayscale 3x3 image
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        System.out.print(image.getRed(i, j) + "\t");
      }
      System.out.println();
    }
    // iterate through all pixels in 3x3 image
    for (int row = 0; row < image.getHeight(); row++) {
      for (int col = 0; col < image.getWidth(); col++) {
        short[][][] reflected = image.reflect(row, col);
        System.out.println("\nx: " + col + ", y: " + row);
        // print out surrounding pixels around current (center) pixel
        for (int i = 0; i < reflected.length; i++) {
          for (int j = 0; j < reflected[0].length; j++) {
            System.out.print(reflected[i][j][0] + "\t");
          }
          System.out.println("");
        }
      }
    }
  }

}
