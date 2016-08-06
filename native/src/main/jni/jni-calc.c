#include <jni.h>
#include <math.h>

float calculateGaussianKernel(int x, int y, int centerX, int centerY, float xSpread, float ySpread, float amplitude) {
   double xTerm = pow(x - centerX, 2) / (2 * pow(xSpread, 2));
   double yTerm = pow(y - centerY, 2) / (2 * pow(ySpread, 2));
   return (float) (amplitude * exp(-(xTerm + yTerm)));
}

/**
 * For each pixel, Calculate Guassian value and pixel color.
 */
void Java_etc_a0la0_osccontroller_anative_NativeHelper_calcMatricies(
        JNIEnv *env, jobject instance,
        int width, int height, int centerX, int centerY, float std, float amplitude,
        jfloatArray valueArray, jintArray pixelArray, int r, int g, int b) {

   jfloat * mutableValueArray = (*env)->GetFloatArrayElements(env, valueArray, 0);
   jint * mutablePixelArray = (*env)->GetIntArrayElements(env, pixelArray, 0);

   int listLength = width * height;
   int i = 0;
   for (i = 0; i < listLength; i++) {

      float value = calculateGaussianKernel(
              (i % width), (i / width),
              centerX, centerY, std, std, amplitude);
      if (value < 0.01) value = 0;

      int red = (int) (r * value);
      int green = (int) (g * value);
      int blue = (int) (b * value);

      if (red < 0) red = 0;
      else if (red > 255) red = 255;
      if (green < 0) green = 0;
      else if (green > 255) green = 255;
      if (blue < 0) blue = 0;
      else if (blue > 255) blue = 255;

      red = (red << 16) & 0x00FF0000;
      green = (green << 8) & 0x0000FF00;
      blue = blue & 0x000000FF;

      int color =  0xFF000000 | red | green | blue;

      mutableValueArray[i] = value;
      mutablePixelArray[i] = color;
   }

   (*env)->ReleaseFloatArrayElements(env, valueArray, mutableValueArray, 0);
   (*env)->ReleaseIntArrayElements(env, pixelArray, mutablePixelArray, 0);
}

/**
 * Given many preset pixel values, use image overlay effect to create one view matrix
 * https://en.wikipedia.org/wiki/Blend_modes#Lighten_Only
 */
void Java_etc_a0la0_osccontroller_anative_NativeHelper_aggregateMatrices(
        JNIEnv *env, jobject instance,
        jintArray pixelBuffer, jintArray pixelValueArray,
        int bufferLength, int numberOfPresets) {

   jint * mutablePixelBuffer = (*env)->GetIntArrayElements(env, pixelBuffer, 0);
   jint * mutablePixelValueArray = (*env)->GetIntArrayElements(env, pixelValueArray, 0);

   int i = 0;
   for (i = 0; i < bufferLength; i++) {
      int r = 0;
      int g = 0;
      int b = 0;

      int j = 0;
      for (j = 0; j < numberOfPresets; j++) {
         int valueIndex = i + (j * bufferLength);
         int pixelValue = mutablePixelValueArray[valueIndex];

         int red = (pixelValue >> 16) & 0xFF;
         int green = (pixelValue >> 8) & 0xFF;
         int blue = (pixelValue >> 0) & 0xFF;

         //Lighten only effect, simply take the max value for each channel
         if (red > r) r = red;
         if (green > g) g = green;
         if (blue > b) b = blue;

      }

      if (r < 0) r = 0;
      else if (r > 255) r = 255;
      if (g < 0) g = 0;
      else if (g > 255) g = 255;
      if (b < 0) b = 0;
      else if (b > 255) b = 255;

      r = (r << 16) & 0x00FF0000;
      g = (g << 8) & 0x0000FF00;
      b = b & 0x000000FF;
      int color =  0xFF000000 | r | g | b;
      mutablePixelBuffer[i] = color;
   }

   (*env)->ReleaseIntArrayElements(env, pixelBuffer, mutablePixelBuffer, 0);
   (*env)->ReleaseIntArrayElements(env, pixelValueArray, mutablePixelValueArray, 0);
}


