/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes
 *  a PixImage object.  Descriptions of the methods you must implement appear
 *  below.  They include constructors of the form
 *
 *      public RunLengthEncoding(int width, int height);
 *      public RunLengthEncoding(int width, int height, int[] red, int[] green,
 *                               int[] blue, int[] runLengths) {
 *      public RunLengthEncoding(PixImage image) {
 *
 *  that create a run-length encoding of a PixImage having the specified width
 *  and height.
 *
 *  The first constructor creates a run-length encoding of a PixImage in which
 *  every pixel is black.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts a PixImage object into a run-length encoding of that image.
 *
 *  See the README file accompanying this project for additional details.
 */

import java.util.Iterator;

public class RunLengthEncoding implements Iterable {

  private int width;
  private int height;
  private DList runs;



  /**
   *  The following methods are required for Part II.
   */

  /**
   *  RunLengthEncoding() (with two parameters) constructs a run-length
   *  encoding of a black PixImage of the specified width and height, in which
   *  every pixel has red, green, and blue intensities of zero.
   *
   *  @param width the width of the image.
   *  @param height the height of the image.
   */

  public RunLengthEncoding(int width, int height) {
    this.width = width;
    this.height = height;
    int[] run = {width*height, 0, 0, 0};
    runs = new DList(run);
  }

  /**
   *  RunLengthEncoding() (with six parameters) constructs a run-length
   *  encoding of a PixImage of the specified width and height.  The runs of
   *  the run-length encoding are taken from four input arrays of equal length.
   *  Run i has length runLengths[i] and RGB intensities red[i], green[i], and
   *  blue[i].
   *
   *  @param width the width of the image.
   *  @param height the height of the image.
   *  @param red is an array that specifies the red intensity of each run.
   *  @param green is an array that specifies the green intensity of each run.
   *  @param blue is an array that specifies the blue intensity of each run.
   *  @param runLengths is an array that specifies the length of each run.
   *
   *  NOTE:  All four input arrays should have the same length (not zero).
   *  All pixel intensities in the first three arrays should be in the range
   *  0...255.  The sum of all the elements of the runLengths array should be
   *  width * height.  (Feel free to quit with an error message if any of these
   *  conditions are not met--though we won't be testing that.)
   */

  public RunLengthEncoding(int width, int height, int[] red, int[] green,
                           int[] blue, int[] runLengths) {

    int sum = 0;  // sum of all elements of runLengths
    this.width = width;
    this.height = height;

    // all four input arrays should be same length and not zero
    if (red.length != 0 && red.length != green.length && 
        red.length != blue.length && red.length != runLengths.length) {
      System.err.println("run/pixel intensity arrays not equal length or zero.");
      return; 
    }

    runs = new DList();

    // sum of all elements of runLengths should be width * height
    for (int run : runLengths) {
      sum += run;
    }
    if (sum != width * height) {
      System.err.println("sum of run lengths does not match width * height.");
      return;
    }

    // constructs the runs
    for (int i = 0; i < runLengths.length; i++) {
      if (red[i] < 0 || red[i] > 255 || green[i] < 0 || green[i] > 255 ||
          blue[i] < 0 || blue[i] > 255) {
        System.err.println("pixel intensity is out of range.");
        return; // pixel intensity should be within range 0...255
      }
      int run[] = {runLengths[i], red[i], green[i], blue[i]};
      runs.insertBack(run);
    }
  }

  /**
   *  getWidth() returns the width of the image that this run-length encoding
   *  represents.
   *
   *  @return the width of the image that this run-length encoding represents.
   */

  public int getWidth() {
    return width;
  }

  /**
   *  getHeight() returns the height of the image that this run-length encoding
   *  represents.
   *
   *  @return the height of the image that this run-length encoding represents.
   */
  public int getHeight() {
    return height;
  }

  /**
   *  iterator() returns a newly created RunIterator that can iterate through
   *  the runs of this RunLengthEncoding.
   *
   *  @return a newly created RunIterator object set to the first run of this
   *  RunLengthEncoding.
   */
  public RunIterator iterator() {
    return new RunIterator(runs);
  }

  /**
   *  toPixImage() converts a run-length encoding of an image into a PixImage
   *  object.
   *
   *  @return the PixImage that this RunLengthEncoding encodes.
   */
  public PixImage toPixImage() {
    PixImage image = new PixImage(width, height);
    RunIterator it = iterator();
    int x = 0;
    int y = 0;
    while (it.hasNext()) {
      int[] run = it.next();
      int runLength = run[0];
      // System.out.println("\n" + runLength);
      // set the pixels for each run
      while (runLength > 0) {
        // System.out.print("x:" + x + " y:" + y + ", ");  // test
        image.setPixel(x, y, (short) run[1], (short) run[2], (short) run[3]);
        x++;
        if (x % width == 0) { // x == width-1, increment y and reset x
          y++;
          x = 0;
        }
        runLength--;
      }
    }
    return image;
  }

  /**
   *  toString() returns a String representation of this RunLengthEncoding.
   *
   *  This method isn't required, but it should be very useful to you when
   *  you're debugging your code.  It's up to you how you represent
   *  a RunLengthEncoding as a String.
   *
   *  @return a String representation of this RunLengthEncoding.
   */
  public String toString() {
    RunIterator it = iterator();
    String result = "RLE - [  ";
    while (it.hasNext()) {
      int[] run = it.next();
      // result += run[1] + "," + run[2] + "," + run[3] + "," + run[0] + "  ";
      result += run[1] + "," + run[0] + "  ";
    }
    return result + "]";
  }


  /**
   *  The following methods are required for Part III.
   */

  /**
   *  RunLengthEncoding() (with one parameter) is a constructor that creates
   *  a run-length encoding of a specified PixImage.
   * 
   *  Note that you must encode the image in row-major format, i.e., the second
   *  pixel should be (1, 0) and not (0, 1).
   *
   *  @param image is the PixImage to run-length encode.
   */
  public RunLengthEncoding(PixImage image) {
    width = image.getWidth();
    height = image.getHeight();
    runs = new DList();

    int runLength = 0;  // length of current run
    int startX = 0; // start of run x coord
    int startY = 0; // start of run y coord

    /*  Loop through entire PixImage:
        1. check if current pixel matches pixel from start of current run
        2a. if true, increment run
        2b. else, create a new run, insert to list, and set runLength, startX, startY */    
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (image.getRed(x, y) == image.getRed(startX, startY)) {
          runLength += 1;
        } else {  // run ended
          int run[] = { runLength,
                        image.getRed(startX, startY),
                        image.getGreen(startX, startY),
                        image.getBlue(startX, startY) };
          runs.insertBack(run);
          runLength = 1;
          startX = x;
          startY = y;
        }
      }
    }
    // insert the last run after loop ends
    if (runLength > 1) {  // more than 1 pixel run when exiting loop
      int[] run = { runLength, 
                    image.getRed(startX, startY), 
                    image.getGreen(startX, startY), 
                    image.getBlue(startX, startY) };
      runs.insertBack(run);
    } else {  // single run of last pixel
      int[] run = { runLength,
                    image.getRed(width-1, height-1),
                    image.getGreen(width-1, height-1),
                    image.getBlue(width-1, height-1) };
      runs.insertBack(run);
    }
    check();
  }

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same RGB intensities, or if the sum of
   *  all run lengths does not equal the number of pixels in the image.
   */
  public void check() {
    RunIterator it = iterator();
    int sum = 0;
    short prevR = Short.MIN_VALUE;
    short prevG = Short.MIN_VALUE;
    short prevB = Short.MIN_VALUE;
    while (it.hasNext()) {
      int[] run = it.next();
      if (prevR == run[1] || prevG == run[2] || prevB == run[3]) {
        System.err.println("two consecutive runs have same RGB intensities.");
        return;
      }
      if (run[0] < 1) {
        System.err.println("run length less than 1.");
      }
      prevR = (short) run[1];
      prevG = (short) run[2];
      prevB = (short) run[3];
      sum += run[0];
    }
    if (sum != width * height) {
      System.err.println("Sum of run lengths (" + sum + ") does not equal " +
                          "number of pixels (" + width*height + ") in the image.");
    }
  }


  /**
   *  The following method is required for Part IV.
   */

  /**
   *  setPixel() modifies this run-length encoding so that the specified color
   *  is stored at the given (x, y) coordinates.  The old pixel value at that
   *  coordinate should be overwritten and all others should remain the same.
   *  The updated run-length encoding should be compressed as much as possible;
   *  there should not be two consecutive runs with exactly the same RGB color.
   *
   *  @param x the x-coordinate of the pixel to modify.
   *  @param y the y-coordinate of the pixel to modify.
   *  @param red the new red intensity to store at coordinate (x, y).
   *  @param green the new green intensity to store at coordinate (x, y).
   *  @param blue the new blue intensity to store at coordinate (x, y).
   */
  public void setPixel(int x, int y, short red, short green, short blue) {

    if (x < 0 || x > width-1 || y < 0 || y > height-1) {
      System.err.println("x and/or y invalid");
      return;
    }
    // find the run pixel is in by comparing its absoulte position to
    // sum of run lengths encountered so far
    int pixelPosition = x + (y * width) + 1;
    int sumRunLength = 0;

    int[] newRun = { 1, red, green, blue }; // new run to insert
    DListNode run = runs.head.next; // run starts at first run after sentinel

    while (sumRunLength + run.item[0] < pixelPosition) {
      sumRunLength += run.item[0];
      run = run.next;
    }
    // if intensity is same for existing pixel and new pixel, do nothing
    if (run.item[1] == red && run.item[2] == green && run.item[3] == blue) {
      return; 
    }

    // pixel is first pixel of run or single pixel run
    if (sumRunLength + 1 == pixelPosition) {
      // not on first run
      if (run.prev != runs.head) {
        if (run.item[0] == 1) {
          // pixel matches adjacent runs pixels
          if (run.next != runs.head &&
              run.prev.item[1] == red   && run.next.item[1] == red &&
              run.prev.item[2] == green && run.next.item[2] == green &&
              run.prev.item[3] == blue  && run.next.item[3] == blue) {
            // combine runs into previous run, link from previous run
            // to run after next run
            run.prev.item[0] += 1 + run.next.item[0];
            run.prev.next = run.next.next;
            run.next.next.prev = run.prev;
          }
          // pixel matches previous run's pixel
          else if (run.prev.item[1] == red && run.prev.item[2] == green &&
                   run.prev.item[3] == blue) {
            run.prev.item[0]++;
            // remove the run
            run.prev.next = run.next;
            run.next.prev = run.prev;
          }
          // pixel matches next run's pixel
          else if (run.next != runs.head && run.next.item[1] == red && 
                   run.next.item[2] == green && run.next.item[3] == blue) {
            run.next.item[0]++;
            // remove the run
            run.prev.next = run.next;
            run.next.prev = run.prev;
          }
          // pixel matches neither run
          else {
            run.item[1] = red;
            run.item[2] = green;
            run.item[3] = blue;
          }
        }
        // run length > 1, matches previous run
        else if (run.prev.item[1] == red && run.prev.item[2] == green &&
                 run.prev.item[3] == blue) {
          run.prev.item[0]++;
          run.item[0]--;
        }
        // run length > 1, previous run doesn't match
        else {
          DListNode before = new DListNode(newRun);
          run.item[0]--;
          // link new node
          before.prev = run.prev;
          before.next = run;
          run.prev.next = before;
          run.prev = before;
        }
      }
      // on first run
      else {
        if (run.item[0] == 1) {
          // check if pixel matches next run's pixels
          if (run.next.item[1] == red && run.next.item[2] == green &&
              run.next.item[3] == blue) {
            run.next.item[0]++;
            // remove the run
            run.prev.next = run.next;
            run.next.prev = run.prev;
          }
          // pixel doesn't match
          else {
            run.item[1] = red;
            run.item[2] = green;
            run.item[3] = blue;
          }
        }
        // run length > 1, insert new run before
        else {
          DListNode before = new DListNode(newRun);
          run.item[0]--;
          // link new node
          before.prev = run.prev;
          before.next = run;
          run.prev.next = before;
          run.prev = before;
        }
      }
    }
    // pixel is last pixel of run (run length > 1)
    else if (sumRunLength + run.item[0] == pixelPosition) {
      // not on last run, and pixel matches next run's pixels
      if (run.next != runs.head && run.next.item[1] == red &&
          run.next.item[2] == green && run.next.item[3] == blue) {
          run.next.item[0]++;
          run.item[0]--;
        }
      // on the last run or pixel does not match next run's pixels,
      // insert new run after
      else {
        DListNode after = new DListNode(newRun);
        run.item[0]--;
        // link new node
        after.prev = run;
        after.next = run.next;
        run.next.prev = after;
        run.next = after;
        }
    }
    // pixel somewhere in middle
    else {
      // nodes split into 3: start, mid, end
      DListNode next = run.next;    // node after the original node
      int runLength = run.item[0];  // length of current run
      int startRunLength = pixelPosition - sumRunLength - 1;
      int endRunLength = runLength - startRunLength - 1;
      int[] endRun = { endRunLength, run.item[1], run.item[2], run.item[3] };

      DListNode start = run;
      start.item[0] = startRunLength;
      DListNode mid = new DListNode(newRun);
      DListNode end = new DListNode(endRun);

      // link the nodes
      start.next = mid;
      mid.prev = start;
      mid.next = end;
      end.prev = mid;
      end.next = next;
    }
    check();
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
   * setAndCheckRLE() sets the given coordinate in the given run-length
   * encoding to the given value and then checks whether the resulting
   * run-length encoding is correct.
   *
   * @param rle the run-length encoding to modify.
   * @param x the x-coordinate to set.
   * @param y the y-coordinate to set.
   * @param intensity the grayscale intensity to assign to pixel (x, y).
   */
  private static void setAndCheckRLE(RunLengthEncoding rle,
                                     int x, int y, int intensity) {
    rle.setPixel(x, y,
                 (short) intensity, (short) intensity, (short) intensity);
    rle.check();
  }

  /**
   * main() runs a series of tests of the run-length encoding code.
   */
  public static void main(String[] args) {
    // Be forwarned that when you write arrays directly in Java as below,
    // each "row" of text is a column of your image--the numbers get
    // transposed.
    PixImage image1 = array2PixImage(new int[][] { { 0, 3, 6 },
                                                   { 1, 4, 7 },
                                                   { 2, 5, 8 } });

    System.out.println("Testing one-parameter RunLengthEncoding constructor " +
                       "on a 3x3 image.  Input image:");
    System.out.print(image1);
    RunLengthEncoding rle1 = new RunLengthEncoding(image1);
    rle1.check();
    System.out.println("Testing getWidth/getHeight on a 3x3 encoding.");
    doTest(rle1.getWidth() == 3 && rle1.getHeight() == 3,
           "RLE1 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x3 encoding.");
    doTest(image1.equals(rle1.toPixImage()),
           "image1 -> RLE1 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 0, 42);
    image1.setPixel(0, 0, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           /*
                       array2PixImage(new int[][] { { 42, 3, 6 },
                                                    { 1, 4, 7 },
                                                    { 2, 5, 8 } })),
           */
           "Setting RLE1[0][0] = 42 fails.");
    
    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 1, 0, 42);
    image1.setPixel(1, 0, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[1][0] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 1, 2);
    image1.setPixel(0, 1, (short) 2, (short) 2, (short) 2);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[0][1] = 2 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 0, 0);
    image1.setPixel(0, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[0][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 2, 2, 7);
    image1.setPixel(2, 2, (short) 7, (short) 7, (short) 7);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[2][2] = 7 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 2, 2, 42);
    image1.setPixel(2, 2, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[2][2] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 1, 2, 42);
    image1.setPixel(1, 2, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[1][2] = 42 fails.");


    PixImage image2 = array2PixImage(new int[][] { { 2, 3, 5 },
                                                   { 2, 4, 5 },
                                                   { 3, 4, 6 } });

    System.out.println("Testing one-parameter RunLengthEncoding constructor " +
                       "on another 3x3 image.  Input image:");
    System.out.print(image2);
    RunLengthEncoding rle2 = new RunLengthEncoding(image2);
    rle2.check();
    System.out.println("Testing getWidth/getHeight on a 3x3 encoding.");
    doTest(rle2.getWidth() == 3 && rle2.getHeight() == 3,
           "RLE2 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x3 encoding.");
    doTest(rle2.toPixImage().equals(image2),
           "image2 -> RLE2 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle2, 0, 1, 2);
    image2.setPixel(0, 1, (short) 2, (short) 2, (short) 2);
    doTest(rle2.toPixImage().equals(image2),
           "Setting RLE2[0][1] = 2 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle2, 2, 0, 2);
    image2.setPixel(2, 0, (short) 2, (short) 2, (short) 2);
    doTest(rle2.toPixImage().equals(image2),
           "Setting RLE2[2][0] = 2 fails.");

    PixImage image3 = array2PixImage(new int[][] { { 0, 5 },
                                                   { 1, 6 },
                                                   { 2, 7 },
                                                   { 3, 8 },
                                                   { 4, 9 } });

    System.out.println("Testing one-parameter RunLengthEncoding constructor " +
                       "on a 5x2 image.  Input image:");
    System.out.print(image3);
    RunLengthEncoding rle3 = new RunLengthEncoding(image3);
    rle3.check();
    System.out.println("Testing getWidth/getHeight on a 5x2 encoding.");
    doTest(rle3.getWidth() == 5 && rle3.getHeight() == 2,
           "RLE3 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 5x2 encoding.");
    doTest(rle3.toPixImage().equals(image3),
           "image3 -> RLE3 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 4, 0, 6);
    image3.setPixel(4, 0, (short) 6, (short) 6, (short) 6);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[4][0] = 6 fails.");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 0, 1, 6);
    image3.setPixel(0, 1, (short) 6, (short) 6, (short) 6);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[0][1] = 6 fails.");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 0, 0, 1);
    image3.setPixel(0, 0, (short) 1, (short) 1, (short) 1);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[0][0] = 1 fails.");


    PixImage image4 = array2PixImage(new int[][] { { 0, 3 },
                                                   { 1, 4 },
                                                   { 2, 5 } });

    System.out.println("Testing one-parameter RunLengthEncoding constructor " +
                       "on a 3x2 image.  Input image:");
    System.out.print(image4);
    RunLengthEncoding rle4 = new RunLengthEncoding(image4);
    rle4.check();
    System.out.println("Testing getWidth/getHeight on a 3x2 encoding.");
    doTest(rle4.getWidth() == 3 && rle4.getHeight() == 2,
           "RLE4 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x2 encoding.");
    doTest(rle4.toPixImage().equals(image4),
           "image4 -> RLE4 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 2, 0, 0);
    image4.setPixel(2, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[2][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 1, 0, 0);
    image4.setPixel(1, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[1][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 1, 0, 1);
    image4.setPixel(1, 0, (short) 1, (short) 1, (short) 1);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[1][0] = 1 fails.");

    // additional cases
    // testSimpleConstructors();
    // testSetPixel();
  }

  private static void testSimpleConstructors() {
    // Be forwarned that when you write arrays directly in Java as below,
    // each "row" of text is a column of your image--the numbers get
    // transposed.
    PixImage image = array2PixImage(new int[][] { { 0, 3, 6 },
                                                   { 1, 4, 7 },
                                                   { 2, 5, 8 } });

    System.out.println("Testing two-parameter RunLengthEncoding constructor " +
                       "on a 3x3 image.  Input image:");
    System.out.print(image);
    RunLengthEncoding rle = new RunLengthEncoding(3,3);
    rle.check();
    System.out.println("RLE:\t" + rle);

    int[] gray = {7, 88, 0};
    int[] run = {3, 5, 4};
    System.out.println("\nTesting six-parameter RunLengthEncoding constructor " +
                       "on a 3x3 image.  Input image:");
    rle = new RunLengthEncoding(4,3,gray,gray,gray,run);
    rle.check();
    System.out.println("RLE:\t" + rle);
    PixImage image1 = rle.toPixImage();
    System.out.println(image1);
  }

  private static void testSetPixel() {
    int[][] a = { { 7,  88, 0 },
                  { 7,  88, 0 },
                  { 7,  88, 0 },
                  { 88, 88, 0 } };
    int[][] b = { { 0, 3, 6 },
                  { 1, 4, 7 },
                  { 2, 5, 8 } };
    int[][] c = { { 2, 3, 5 },
                  { 2, 4, 5 },
                  { 3, 4, 6 } };
    int[][] d = { { 0, 5 },
                  { 1, 6 },
                  { 2, 7 },
                  { 3, 8 },
                  { 4, 9 } };
    int[][] e = { { 0, 3 },
                  { 1, 4 },
                  { 2, 5 } };
    testSetPixel(a);
    testSetPixel(b);
    testSetPixel(c);
    testSetPixel(d);
    testSetPixel(e);
  }

  private static void testSetPixel(int[][] a) {
    // Be forwarned that when you write arrays directly in Java as below,
    // each "row" of text is a column of your image--the numbers get
    // transposed.
    PixImage originalImage = array2PixImage(a);
    int errorCount = 0;
    for (int y = 0; y < originalImage.getHeight(); y++) {
      for (int x = 0; x < originalImage.getWidth(); x++) {
        PixImage image = array2PixImage(a);
        RunLengthEncoding rle = new RunLengthEncoding(originalImage);

        rle.setPixel(x,y, (short) 255, (short) 255, (short) 255);
        image.setPixel(x,y, (short) 255, (short) 255, (short) 255);

        if (!image.equals(rle.toPixImage())) {
          errorCount++;
          System.out.println("Error #" + errorCount);

          System.out.print("*** ORIGINAL IMAGE ***\n" + originalImage);
          System.out.println("*** ORIGINAL RLE ***\n" + rle + "\n");

          System.out.println("x: " + x + ", y: " + y);
          System.out.println("*** NEW RLE ***\n" + rle);
          System.out.println("*** NEW IMAGE ***\n" + rle.toPixImage());

          System.out.print("*** EXPECTED IMAGE ***\n" + image);
          System.out.println("*** EXPECTED RLE ***\n" + new RunLengthEncoding(image));
        }
        System.out.println(new RunLengthEncoding(image));
      }
    }
  }
}
