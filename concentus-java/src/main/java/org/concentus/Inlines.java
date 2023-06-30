/* Copyright (c) 2006-2011 Skype Limited. 
   Copyright (c) 2007-2008 CSIRO
   Copyright (c) 2007-2011 Xiph.Org Foundation
   Ported to Java by Logan Stromberg

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions
   are met:

   - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

   - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.

   - Neither the name of Internet Society, IETF or IETF Trust, nor the
   names of specific contributors, may be used to endorse or promote
   products derived from this software without specific prior written
   permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
   OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.concentus;

class Inlines {

    static void OpusAssert(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
        //Debug.Assert(condition);
    }

    static void OpusAssert(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        //Debug.Assert(condition, message);
    }

    static long CapToUInt32(long val) {
        return (0xFFFFFFFFL & (int) val);
    }

    static long CapToUInt32(int val) {
        return val;
    }

    // CELT-SPECIFIC INLINES
    //        /** Multiply a 16-bit signed value by a 16-bit unsigned value. The result is a 32-bit signed value */
    //#define MULT16_16SU(a,b) ((opus_val32)(opus_val16)(a)*(opus_val32)(opus_uint16)(b))
    static int MULT16_16SU(int a, int b) {
        return ((int) (short) (a) * (int) (b & 0xFFFF));
    }

    //        /** 16x32 multiplication, followed by a 16-bit shift right. Results fits in 32 bits */
    //#define MULT16_32_Q16(a,b) ADD32(MULT16_16((a),SHR((b),16)), SHR(MULT16_16SU((a),((b)&0x0000ffff)),16))
    static int MULT16_32_Q16(short a, int b) {
        return ADD32(MULT16_16((a), SHR((b), 16)), SHR(MULT16_16SU((a), ((b) & 0x0000ffff)), 16));
    }

    static int MULT16_32_Q16(int a, int b) {
        return ADD32(MULT16_16((a), SHR((b), 16)), SHR(MULT16_16SU((a), ((b) & 0x0000ffff)), 16));
    }

    //        /** 16x32 multiplication, followed by a 16-bit shift right (round-to-nearest). Results fits in 32 bits */
    //#define MULT16_32_P16(a,b) ADD32(MULT16_16((a),SHR((b),16)), PSHR(MULT16_16SU((a),((b)&0x0000ffff)),16))
    static int MULT16_32_P16(short a, int b) {
        return ADD32(MULT16_16((a), SHR((b), 16)), PSHR(MULT16_16SU((a), ((b) & 0x0000ffff)), 16));
    }

    static int MULT16_32_P16(int a, int b) {
        return ADD32(MULT16_16((a), SHR((b), 16)), PSHR(MULT16_16SU((a), ((b) & 0x0000ffff)), 16));
    }

    //        /** 16x32 multiplication, followed by a 15-bit shift right. Results fits in 32 bits */
    static int MULT16_32_Q15(short a, int b) {
        return ((a * (b >> 16)) << 1) + ((a * (b & 0xFFFF)) >> 15);
        //return ADD32(SHL(MULT16_16((a), SHR((b), 16)), 1), SHR(MULT16_16SU((a), (ushort)((b) & 0x0000ffff)), 15));
    }

    static int MULT16_32_Q15(int a, int b) {
        return ((a * (b >> 16)) << 1) + ((a * (b & 0xFFFF)) >> 15);
        //return ADD32(SHL(MULT16_16((a), SHR((b), 16)), 1), SHR(MULT16_16SU((a), (uint)((b) & 0x0000ffff)), 15));
    }

    //        /** 32x32 multiplication, followed by a 31-bit shift right. Results fits in 32 bits */
    //#define MULT32_32_Q31(a,b) ADD32(ADD32(SHL(MULT16_16(SHR((a),16),SHR((b),16)),1), SHR(MULT16_16SU(SHR((a),16),((b)&0x0000ffff)),15)), SHR(MULT16_16SU(SHR((b),16),((a)&0x0000ffff)),15))
    static int MULT32_32_Q31(int a, int b) {
        return ADD32(ADD32(SHL(MULT16_16(SHR((a), 16), SHR((b), 16)), 1), SHR(MULT16_16SU(SHR((a), 16), ((b) & 0x0000ffff)), 15)), SHR(MULT16_16SU(SHR((b), 16), ((a) & 0x0000ffff)), 15));
    }

    // "Compile-time" (not really) conversion of float constant to 16-bit value
    static short QCONST16(float x, int bits) {
        return ((short) (0.5 + (x) * (((int) 1) << (bits))));
    }

    // "Compile-time" (not really) conversion of float constant to 32-bit value
    static int QCONST32(float x, int bits) {
        return ((int) (0.5 + (x) * (((int) 1) << (bits))));
    }

    //        /** Negate a 16-bit value */
    static short NEG16(short x) {
        return (short) (0 - x);
    }

    static int NEG16(int x) {
        return 0 - x;
    }

    //        /** Negate a 32-bit value */
    static int NEG32(int x) {
        return 0 - x;
    }

    //        /** Change a 32-bit value into a 16-bit value. The value is assumed to fit in 16-bit, otherwise the result is undefined */
    static short EXTRACT16(int x) {
        return ((short) x);
    }

    //        /** Change a 16-bit value into a 32-bit value */
    static int EXTEND32(short x) {
        return (int) x;
    }

    static int EXTEND32(int x) {
        return x;
    }

    //        /** Arithmetic shift-right of a 16-bit value */
    static short SHR16(short a, int shift) {
        return (short) ((a) >> (shift));
    }

    static int SHR16(int a, int shift) {
        return ((a) >> (shift));
    }

    //        /** Arithmetic shift-left of a 16-bit value */
    static short SHL16(short a, int shift) {
        return ((short) ((a & 0xFFFF) << shift));
    }

    static int SHL16(int a, int shift) {
        return (int) (0xFFFFFFFF & ((long) a << shift));
    }

    //        /** Arithmetic shift-right of a 32-bit value */
    static int SHR32(int a, int shift) {
        return a >> shift;
    }

    //        /** Arithmetic shift-left of a 32-bit value */
    static int SHL32(int a, int shift) {
        return (int) (0xFFFFFFFF & ((long) a << shift));
    }

    //        /** 32-bit arithmetic shift right with rounding-to-nearest instead of rounding down */
    static int PSHR32(int a, int shift) {
        return (SHR32((a) + ((EXTEND32(1) << ((shift)) >> 1)), shift));
    }

    static short PSHR16(short a, int shift) {
        return SHR16((short) (a + (1 << (shift) >> 1)), shift);
    }

    static int PSHR16(int a, int shift) {
        return SHR32((a + (1 << (shift) >> 1)), shift);
    }

    //        /** 32-bit arithmetic shift right where the argument can be negative */
    static int VSHR32(int a, int shift) {
        return (((shift) > 0) ? SHR32(a, shift) : SHL32(a, -(shift)));
    }

    //        /** "RAW" macros, should not be used outside of this header file */
    private static int SHR(int a, int shift) {
        return ((a) >> (shift));
    }

    private static int SHL(int a, int shift) {
        return SHL32(a, shift);
    }

    private static int SHR(short a, int shift) {
        return ((a) >> (shift));
    }

    private static int SHL(short a, int shift) {
        return SHL32(a, shift);
    }

    private static int PSHR(int a, int shift) {
        return (SHR((a) + ((EXTEND32(1) << ((shift)) >> 1)), shift));
    }

    static int SATURATE(int x, int a) {
        return (((x) > (a) ? (a) : (x) < -(a) ? -(a) : (x)));
    }

    static short SATURATE16(int x) {
        return (EXTRACT16((x) > 32767 ? 32767 : (x) < -32768 ? -32768 : (x)));
    }

    //        /** Shift by a and round-to-neareast 32-bit value. Result is a 16-bit value */
    static short ROUND16(short x, short a) {
        return (EXTRACT16(PSHR32((x), (a))));
    }

    static int ROUND16(int x, int a) {
        return PSHR32((x), (a));
    }

    static int PDIV32(int a, int b) {
        return a / b;
    }

    //        /** Divide by two */
    // fixme: can this be optimized?
    static short HALF16(short x) {
        return (SHR16(x, 1));
    }

    static int HALF16(int x) {
        return (SHR32(x, 1));
    }

    static int HALF32(int x) {
        return (SHR32(x, 1));
    }

    //        /** Add two 16-bit values */
    static short ADD16(short a, short b) {
        return ((short) ((short) (a) + (short) (b)));
    }

    static int ADD16(int a, int b) {
        return (a + b);
    }

    //        /** Subtract two 16-bit values */
    static short SUB16(short a, short b) {
        return ((short) ((short) (a) - (short) (b)));
    }

    static int SUB16(int a, int b) {
        return (a - b);
    }

    //        /** Add two 32-bit values */
    static int ADD32(int a, int b) {
        return ((int) (a) + (int) (b));
    }

    //        /** Subtract two 32-bit values */
    static int SUB32(int a, int b) {
        return ((int) (a) - (int) (b));
    }

    //        /** 16x16 multiplication where the result fits in 16 bits */
    //#define MULT16_16_16(a,b)     ((((opus_val16)(a))*((opus_val16)(b))))
    static short MULT16_16_16(short a, short b) {
        return (short) (((((short) (a)) * ((short) (b)))));
    }

    static int MULT16_16_16(int a, int b) {
        return (a * b);
    }

    //        /* (opus_val32)(opus_val16) gives TI compiler a hint that it's 16x16->32 multiply */
    //        /** 16x16 multiplication where the result fits in 32 bits */
    //#define MULT16_16(a,b)     (((opus_val32)(opus_val16)(a))*((opus_val32)(opus_val16)(b)))
    static int MULT16_16(int a, int b) {
        return a * b;
    }

    static int MULT16_16(short a, short b) {
        return a * b;
    }

    //        /** 16x16 multiply-add where the result fits in 32 bits */
    //#define MAC16_16(c,a,b) (ADD32((c),MULT16_16((a),(b))))
    static int MAC16_16(short c, short a, short b) {
        return c + (a * b);
    }

    static int MAC16_16(int c, short a, short b) {
        return c + (a * b);
    }

    static int MAC16_16(int c, int a, int b) {
        return c + (a * b);
    }

    //        /** 16x32 multiply, followed by a 15-bit shift right and 32-bit add.
    //            b must fit in 31 bits.
    //            Result fits in 32 bits. */
    //#define MAC16_32_Q15(c,a,b) ADD32((c),ADD32(MULT16_16((a),SHR((b),15)), SHR(MULT16_16((a),((b)&0x00007fff)),15)))
    static int MAC16_32_Q15(int c, short a, short b) {
        return ADD32((c), ADD32(MULT16_16((a), SHR((b), 15)), SHR(MULT16_16((a), ((b) & 0x00007fff)), 15)));
    }

    static int MAC16_32_Q15(int c, int a, int b) {
        return ADD32((c), ADD32(MULT16_16((a), SHR((b), 15)), SHR(MULT16_16((a), ((b) & 0x00007fff)), 15)));
    }

    //        /** 16x32 multiplication, followed by a 16-bit shift right and 32-bit add.
    //            Results fits in 32 bits */
    //#define MAC16_32_Q16(c,a,b) ADD32((c),ADD32(MULT16_16((a),SHR((b),16)), SHR(MULT16_16SU((a),((b)&0x0000ffff)),16)))
    static int MAC16_32_Q16(int c, short a, short b) {
        return ADD32((c), ADD32(MULT16_16((a), SHR((b), 16)), SHR(MULT16_16SU((a), ((b) & 0x0000ffff)), 16)));
    }

    static int MAC16_32_Q16(int c, int a, int b) {
        return ADD32((c), ADD32(MULT16_16((a), SHR((b), 16)), SHR(MULT16_16SU((a), ((b) & 0x0000ffff)), 16)));
    }

    //#define MULT16_16_Q11_32(a,b) (SHR(MULT16_16((a),(b)),11))
    static int MULT16_16_Q11_32(short a, short b) {
        return (SHR(MULT16_16((a), (b)), 11));
    }

    static int MULT16_16_Q11_32(int a, int b) {
        return (SHR(MULT16_16((a), (b)), 11));
    }

    //#define MULT16_16_Q11(a,b) (SHR(MULT16_16((a),(b)),11))
    static short MULT16_16_Q11(short a, short b) {
        return (short) ((SHR(MULT16_16((a), (b)), 11)));
    }

    static int MULT16_16_Q11(int a, int b) {
        return (SHR(MULT16_16((a), (b)), 11));
    }

    //#define MULT16_16_Q13(a,b) (SHR(MULT16_16((a),(b)),13))
    static short MULT16_16_Q13(short a, short b) {
        return (short) ((SHR(MULT16_16((a), (b)), 13)));
    }

    static int MULT16_16_Q13(int a, int b) {
        return (SHR(MULT16_16((a), (b)), 13));
    }

    //#define MULT16_16_Q14(a,b) (SHR(MULT16_16((a),(b)),14))
    static short MULT16_16_Q14(short a, short b) {
        return (short) ((SHR(MULT16_16((a), (b)), 14)));
    }

    static int MULT16_16_Q14(int a, int b) {
        return (SHR(MULT16_16((a), (b)), 14));
    }

    //#define MULT16_16_Q15(a,b) (SHR(MULT16_16((a),(b)),15))
    static short MULT16_16_Q15(short a, short b) {
        return (short) ((SHR(MULT16_16((a), (b)), 15)));
    }

    static int MULT16_16_Q15(int a, int b) {
        return (SHR(MULT16_16((a), (b)), 15));
    }

    //#define MULT16_16_P13(a,b) (SHR(ADD32(4096,MULT16_16((a),(b))),13))
    static short MULT16_16_P13(short a, short b) {
        return (short) ((SHR(ADD32(4096, MULT16_16((a), (b))), 13)));
    }

    static int MULT16_16_P13(int a, int b) {
        return (SHR(ADD32(4096, MULT16_16((a), (b))), 13));
    }

    //#define MULT16_16_P14(a,b) (SHR(ADD32(8192,MULT16_16((a),(b))),14))
    static short MULT16_16_P14(short a, short b) {
        return (short) ((SHR(ADD32(8192, MULT16_16((a), (b))), 14)));
    }

    static int MULT16_16_P14(int a, int b) {
        return (SHR(ADD32(8192, MULT16_16((a), (b))), 14));
    }

    //#define MULT16_16_P15(a,b) (SHR(ADD32(16384,MULT16_16((a),(b))),15))
    static short MULT16_16_P15(short a, short b) {
        return (short) ((SHR(ADD32(16384, MULT16_16((a), (b))), 15)));
    }

    static int MULT16_16_P15(int a, int b) {
        return (SHR(ADD32(16384, MULT16_16((a), (b))), 15));
    }

    //        /** Divide a 32-bit value by a 16-bit value. Result fits in 16 bits */
    //#define DIV32_16(a,b) ((opus_val16)(((opus_val32)(a))/((opus_val16)(b))))
    static short DIV32_16(int a, short b) {
        return (short) (((short) (((int) (a)) / ((short) (b)))));
    }

    static int DIV32_16(int a, int b) {
        return a / b;
    }

    //        /** Divide a 32-bit value by a 32-bit value. Result fits in 32 bits */
    //#define DIV32(a,b) (((opus_val32)(a))/((opus_val32)(b)))
    static int DIV32(int a, int b) {
        return a / b;
    }

    // identical to silk_SAT16 - saturate operation
    static short SAT16(int x) {
        return (short) (x > 32767 ? 32767 : x < -32768 ? -32768 : (short) x);
    }

    static short SIG2WORD16(int x) {
        x = PSHR32(x, 12);
        x = MAX32(x, -32768);
        x = MIN32(x, 32767);
        return EXTRACT16(x);
    }

    static short MIN(short a, short b) {
        return ((a) < (b) ? (a) : (b));
    }

    static short MAX(short a, short b) {
        return ((a) > (b) ? (a) : (b));
    }

    static short MIN16(short a, short b) {
        return ((a) < (b) ? (a) : (b));
    }

    static short MAX16(short a, short b) {
        return ((a) > (b) ? (a) : (b));
    }

    static int MIN16(int a, int b) {
        return ((a) < (b) ? (a) : (b));
    }

    static int MAX16(int a, int b) {
        return ((a) > (b) ? (a) : (b));
    }

    static float MIN16(float a, float b) {
        return ((a) < (b) ? (a) : (b));
    }

    static float MAX16(float a, float b) {
        return ((a) > (b) ? (a) : (b));
    }

    static int MIN(int a, int b) {
        return ((a) < (b) ? (a) : (b));
    }

    static int MAX(int a, int b) {
        return ((a) > (b) ? (a) : (b));
    }

    static int IMIN(int a, int b) {
        return ((a) < (b) ? (a) : (b));
    }

    static long IMIN(long a, long b) {
        return ((a) < (b) ? (a) : (b));
    }

    static int IMAX(int a, int b) {
        return ((a) > (b) ? (a) : (b));
    }

    static int MIN32(int a, int b) {
        return ((a) < (b) ? (a) : (b));
    }

    static int MAX32(int a, int b) {
        return ((a) > (b) ? (a) : (b));
    }

    static float MIN32(float a, float b) {
        return ((a) < (b) ? (a) : (b));
    }

    static float MAX32(float a, float b) {
        return ((a) > (b) ? (a) : (b));
    }

    static int ABS16(int x) {
        return ((x) < 0 ? (-(x)) : (x));
    }

    static float ABS16(float x) {
        return ((x) < 0 ? (-(x)) : (x));
    }

    static short ABS16(short x) {
        return (short) (((x) < 0 ? (-(x)) : (x)));
    }

    static int ABS32(int x) {
        return ((x) < 0 ? (-(x)) : (x));
    }

    static int celt_udiv(int n, int d) {
        Inlines.OpusAssert(d > 0);
        return n / d;
    }

    static int celt_sudiv(int n, int d) {
        Inlines.OpusAssert(d > 0);
        return n / d;
    }

    //#define celt_div(a,b) MULT32_32_Q31((opus_val32)(a),celt_rcp(b))
    static int celt_div(int a, int b) {
        return MULT32_32_Q31((int) (a), celt_rcp(b));
    }

    /**
     * Integer log in base2. Undefined for zero and negative numbers
     */
    static int celt_ilog2(int x) {
        Inlines.OpusAssert(x > 0, "celt_ilog2() only defined for strictly positive numbers");
        return (EC_ILOG((long) x) - 1);
    }

    /**
     * Integer log in base2. Defined for zero, but not for negative numbers
     */
    static int celt_zlog2(int x) {
        return x <= 0 ? 0 : celt_ilog2(x);
    }

    static int celt_maxabs16(int[] x, int x_ptr, int len) {
        int i;
        int maxval = 0;
        int minval = 0;
        for (i = x_ptr; i < len + x_ptr; i++) {
            maxval = MAX32(maxval, x[i]);
            minval = MIN32(minval, x[i]);
        }
        return MAX32(EXTEND32(maxval), -EXTEND32(minval));
    }

    static int celt_maxabs32(int[] x, int x_ptr, int len) {
        int i;
        int maxval = 0;
        int minval = 0;
        for (i = x_ptr; i < x_ptr + len; i++) {
            maxval = MAX32(maxval, x[i]);
            minval = MIN32(minval, x[i]);
        }
        return MAX32(maxval, 0 - minval);
    }

    static short celt_maxabs32(short[] x, int x_ptr, int len) {
        int i;
        short maxval = 0;
        short minval = 0;
        for (i = x_ptr; i < x_ptr + len; i++) {
            maxval = MAX16(maxval, x[i]);
            minval = MIN16(minval, x[i]);
        }
        return MAX(maxval, (short) (0 - minval));
    }

    /// <summary>
    /// Multiplies two 16-bit fractional values. Bit-exactness of this macro is important
    /// </summary>
    /// <param name="a"></param>
    /// <param name="b"></param>
    /// <returns></returns>
    static int FRAC_MUL16(int a, int b) {
        return ((16384 + ((int) ((short) a * (short) b))) >> 15);
    }

    /// <summary>
    /// Compute floor(sqrt(_val)) with exact arithmetic.
    /// This has been tested on all possible 32-bit inputs.
    /// </summary>
    /// <param name="_val"></param>
    /// <returns></returns>
    static int isqrt32(long _val) {
        int b;
        int g;
        int bshift;
        /*Uses the second method from
           http://www.azillionmonkeys.com/qed/sqroot.html
          The main idea is to search for the largest binary digit b such that
           (g+b)*(g+b) <= _val, and add it to the solution g.*/
        g = 0;
        bshift = (EC_ILOG(_val) - 1) >> 1;
        b = 1 << bshift;
        do {
            long t;
            t = ((g << 1) + b) << bshift;
            if (t <= _val) {
                g += b;
                _val -= t;
            }
            b >>= 1;
            bshift--;
        } while (bshift >= 0);
        return g;
    }

    private static short[] sqrt_C = {23175, 11561, -3011, 1699, -664};

    /**
     * Sqrt approximation (QX input, QX/2 output)
     */
    static int celt_sqrt(int x) {
        int k;
        short n;
        int rt;

        if (x == 0) {
            return 0;
        } else if (x >= 1073741824) {
            return 32767;
        }
        k = (celt_ilog2(x) >> 1) - 7;
        x = VSHR32(x, 2 * k);
        n = (short) (x - 32768);
        rt = ADD16(sqrt_C[0], MULT16_16_Q15(n, ADD16(sqrt_C[1], MULT16_16_Q15(n, ADD16(sqrt_C[2],
                MULT16_16_Q15(n, ADD16(sqrt_C[3], MULT16_16_Q15(n, (sqrt_C[4])))))))));
        rt = VSHR32(rt, 7 - k);
        return rt;
    }

    /**
     * Reciprocal approximation (Q15 input, Q16 output)
     */
    static int celt_rcp(int x) {
        int i;
        int n;
        int r;
        Inlines.OpusAssert(x > 0, "celt_rcp() only defined for positive values");
        i = celt_ilog2(x);
        /* n is Q15 with range [0,1). */
        n = VSHR32(x, i - 15) - 32768;
        /* Start with a linear approximation:
           r = 1.8823529411764706-0.9411764705882353*n.
           The coefficients and the result are Q14 in the range [15420,30840].*/
        r = ADD16(30840, MULT16_16_Q15(-15420, n));
        /* Perform two Newton iterations:
           r -= r*((r*n)-1.Q15)
              = r*((r*n)+(r-1.Q15)). */
        r = SUB16(r, MULT16_16_Q15(r,
                ADD16(MULT16_16_Q15(r, n), ADD16(r, -32768))));
        /* We subtract an extra 1 in the second iteration to avoid overflow; it also
            neatly compensates for truncation error in the rest of the process. */
        r = SUB16(r, ADD16(1, MULT16_16_Q15(r,
                ADD16(MULT16_16_Q15(r, n), ADD16(r, -32768)))));
        /* r is now the Q15 solution to 2/(n+1), with a maximum relative error
            of 7.05346E-5, a (relative) RMSE of 2.14418E-5, and a peak absolute
            error of 1.24665/32768. */
        return VSHR32(EXTEND32(r), i - 16);
    }

    /**
     * Reciprocal sqrt approximation in the range [0.25,1) (Q16 in, Q14 out)
     */
    static int celt_rsqrt_norm(int x) {
        int n;
        int r;
        int r2;
        int y;
        /* Range of n is [-16384,32767] ([-0.5,1) in Q15). */
        n = x - 32768;
        /* Get a rough initial guess for the root.
           The optimal minimax quadratic approximation (using relative error) is
            r = 1.437799046117536+n*(-0.823394375837328+n*0.4096419668459485).
           Coefficients here, and the final result r, are Q14.*/
        r = ADD16(23557, MULT16_16_Q15(n, ADD16(-13490, MULT16_16_Q15(n, 6713))));
        /* We want y = x*r*r-1 in Q15, but x is 32-bit Q16 and r is Q14.
           We can compute the result from n and r using Q15 multiplies with some
            adjustment, carefully done to avoid overflow.
           Range of y is [-1564,1594]. */
        r2 = MULT16_16_Q15(r, r);
        y = SHL16(SUB16(ADD16(MULT16_16_Q15(r2, n), r2), 16384), 1);
        /* Apply a 2nd-order Householder iteration: r += r*y*(y*0.375-0.5).
           This yields the Q14 reciprocal square root of the Q16 x, with a maximum
            relative error of 1.04956E-4, a (relative) RMSE of 2.80979E-5, and a
            peak absolute error of 2.26591/16384. */
        return ADD16(r, MULT16_16_Q15(r, MULT16_16_Q15(y,
                SUB16(MULT16_16_Q15(y, 12288), 16384))));
    }

    static int frac_div32(int a, int b) {
        int rcp;
        int result, rem;
        int shift = celt_ilog2(b) - 29;
        a = VSHR32(a, shift);
        b = VSHR32(b, shift);
        /* 16-bit reciprocal */
        rcp = ROUND16(celt_rcp(ROUND16(b, 16)), 3);
        result = MULT16_32_Q15(rcp, a);
        rem = PSHR32(a, 2) - MULT32_32_Q31(result, b);
        result = ADD32(result, SHL32(MULT16_32_Q15(rcp, rem), 2));
        if (result >= 536870912) /*  2^29 */ {
            return 2147483647;          /*  2^31 - 1 */
        } else if (result <= -536870912) /* -2^29 */ {
            return -2147483647;         /* -2^31 */
        } else {
            return SHL32(result, 2);
        }
    }

    private static short log2_C0 = -6801 + (1 << (3));

    /**
     * Base-2 logarithm approximation (log2(x)). (Q14 input, Q10 output)
     */
    static int celt_log2(int x) {
        int i;
        int n, frac;
        /* -0.41509302963303146, 0.9609890551383969, -0.31836011537636605,
            0.15530808010959576, -0.08556153059057618 */
        if (x == 0) {
            return -32767;
        }
        i = celt_ilog2(x);
        n = VSHR32(x, i - 15) - 32768 - 16384;
        frac = ADD16(log2_C0, MULT16_16_Q15(n, ADD16(15746, MULT16_16_Q15(n, ADD16(-5217, MULT16_16_Q15(n, ADD16(2545, MULT16_16_Q15(n, -1401))))))));
        return SHL16((short) (i - 13), 10) + SHR16(frac, 4);
    }

    static int celt_exp2_frac(int x) {
        int frac;
        frac = SHL16(x, 4);
        return ADD16(16383, MULT16_16_Q15(frac, ADD16(22804, MULT16_16_Q15(frac, ADD16(14819, MULT16_16_Q15(10204, frac))))));
    }

    /**
     * Base-2 exponential approximation (2^x). (Q10 input, Q16 output)
     */
    static int celt_exp2(int x) {
        int integer;
        int frac;
        integer = SHR16(x, 10);
        if (integer > 14) {
            return 0x7f000000;
        } else if (integer < -15) {
            return 0;
        }
        frac = (short) (celt_exp2_frac((short) (x - SHL16((short) (integer), 10))));
        return VSHR32(EXTEND32(frac), -integer - 2);
    }

    /* Atan approximation using a 4th order polynomial. Input is in Q15 format
       and normalized by pi/4. Output is in Q15 format */
    static int celt_atan01(int x) {
        return MULT16_16_P15(x, ADD32(32767, MULT16_16_P15(x, ADD32(-21, MULT16_16_P15(x, ADD32(-11943, MULT16_16_P15(4936, x)))))));
    }

    /* atan2() approximation valid for positive input values */
    static int celt_atan2p(int y, int x) {
        if (y < x) {
            int arg;
            arg = celt_div(SHL32(EXTEND32(y), 15), x);
            if (arg >= 32767) {
                arg = 32767;
            }
            return SHR32(celt_atan01(EXTRACT16(arg)), 1);
        } else {
            int arg;
            arg = celt_div(SHL32(EXTEND32(x), 15), y);
            if (arg >= 32767) {
                arg = 32767;
            }
            return 25736 - SHR16(celt_atan01(EXTRACT16(arg)), 1);
        }
    }

    static int celt_cos_norm(int x) {
        x = x & 0x0001ffff;
        if (x > SHL32(EXTEND32(1), 16)) {
            x = SUB32(SHL32(EXTEND32(1), 17), x);
        }
        if ((x & 0x00007fff) != 0) {
            if (x < SHL32(EXTEND32(1), 15)) {
                return _celt_cos_pi_2(EXTRACT16(x));
            } else {
                return NEG32(_celt_cos_pi_2(EXTRACT16(65536 - x))); // opus bug: should be neg32?
            }
        } else if ((x & 0x0000ffff) != 0) {
            return 0;
        } else if ((x & 0x0001ffff) != 0) {
            return -32767;
        } else {
            return 32767;
        }
    }

    static int _celt_cos_pi_2(int x) {
        int x2;

        x2 = MULT16_16_P15(x, x);
        return ADD32(1, MIN32(32766, ADD32(SUB16(32767, x2), MULT16_16_P15(x2, ADD32(-7651, MULT16_16_P15(x2, ADD32(8277, MULT16_16_P15(-626, x2))))))));
    }

    static short FLOAT2INT16(float x) {
        x = x * CeltConstants.CELT_SIG_SCALE;
        if (x < Short.MIN_VALUE) {
            x = Short.MIN_VALUE;
        }
        if (x > Short.MAX_VALUE) {
            x = Short.MAX_VALUE;
        }
        return (short) x;
    }

    // SILK-SPECIFIC INLINES
    /// <summary>
    /// Rotate a32 right by 'rot' bits. Negative rot values result in rotating
    /// left. Output is 32bit int.
    /// </summary>
    /// <param name="a32"></param>
    /// <param name="rot"></param>
    /// <returns></returns>
    static int silk_ROR32(int a32, int rot) {
        int m = (0 - rot);
        if (rot == 0) {
            return a32;
        } else if (rot < 0) {
            return ((a32 << m) | (a32 >> (32 - m)));
        } else {
            return ((a32 << (32 - rot)) | (a32 >> rot));
        }
    }

    static int silk_MUL(int a32, int b32) {
        int ret = a32 * b32;
        return ret;
    }

    static int silk_MLA(int a32, int b32, int c32) {
        int ret = silk_ADD32((a32), ((b32) * (c32)));
        Inlines.OpusAssert((long) ret == (long) a32 + (long) b32 * (long) c32);
        return ret;
    }

    /// <summary>
    /// ((a32 >> 16)  * (b32 >> 16))
    /// </summary>
    /// <param name="a32"></param>
    /// <param name="b32"></param>
    /// <returns></returns>
    static int silk_SMULTT(int a32, int b32) {
        return ((a32 >> 16) * (b32 >> 16));
    }

    static int silk_SMLATT(int a32, int b32, int c32) {
        return silk_ADD32((a32), ((b32) >> 16) * ((c32) >> 16));
    }

    static long silk_SMLALBB(long a64, short b16, short c16) {
        return silk_ADD64((a64), (long) ((int) (b16) * (int) (c16)));
    }

    static long silk_SMULL(int a32, int b32) {
        return (long) a32 * (long) b32;
    }

    /// <summary>
    /// Adds two signed 32-bit values in a way that can overflow, while not relying on undefined behaviour
    /// (just standard two's complement implementation-specific behaviour)
    /// </summary>
    /// <param name="a"></param>
    /// <param name="b"></param>
    /// <returns></returns>
    static int silk_ADD32_ovflw(int a, int b) {
        return (int) ((long) a + b);
    }

    static int silk_ADD32_ovflw(long a, long b) {
        return ((int) (a + b));
    }

    /// <summary>
    /// Subtracts two signed 32-bit values in a way that can overflow, while not relying on undefined behaviour
    /// (just standard two's complement implementation-specific behaviour)
    /// </summary>
    /// <param name="a"></param>
    /// <param name="b"></param>
    /// <returns></returns>
    static int silk_SUB32_ovflw(int a, int b) {
        return (int) ((long) a - b);
    }

    /// <summary>
    /// Multiply-accumulate macros that allow overflow in the addition (ie, no asserts in debug mode)
    /// </summary>
    /// <param name="a32"></param>
    /// <param name="b32"></param>
    /// <param name="c32"></param>
    /// <returns></returns>
    static int silk_MLA_ovflw(int a32, int b32, int c32) {
        return (int) silk_ADD32_ovflw((long) a32, (long) b32 * c32);
    }

    static int silk_SMLABB_ovflw(int a32, int b32, int c32) {
        return ((silk_ADD32_ovflw((a32), ((int) ((short) (b32))) * (int) ((short) (c32)))));
    }

    static int silk_SMULBB(int a32, int b32) {
        return ((int) ((short) a32) * (int) ((short) b32));
    }

    /// <summary>
    /// (a32 * (int)((short)(b32))) >> 16 output have to be 32bit int
    /// </summary>
    /// <param name="a32"></param>
    /// <param name="b32"></param>
    /// <returns></returns>
    static int silk_SMULWB(int a32, int b32) {
        return ((int) (((a32 * (long) (((short) b32))) >> 16)));
    }

    static int silk_SMLABB(int a32, int b32, int c32) {
        return ((a32) + ((int) ((short) b32)) * (int) ((short) c32));
    }

    static int silk_DIV32_16(int a32, int b32) {
        return a32 / b32;
    }

    static int silk_DIV32(int a32, int b32) {
        return a32 / b32;
    }

    static short silk_ADD16(short a, short b) {
        short ret = (short) (a + b);
        return ret;
    }

    static int silk_ADD32(int a, int b) {
        int ret = a + b;
        return ret;
    }

    static long silk_ADD64(long a, long b) {
        long ret = a + b;
        Inlines.OpusAssert(ret == silk_ADD_SAT64(a, b));
        return ret;
    }

    static short silk_SUB16(short a, short b) {
        short ret = (short) (a - b);
        Inlines.OpusAssert(ret == silk_SUB_SAT16(a, b));
        return ret;
    }

    static int silk_SUB32(int a, int b) {
        int ret = a - b;
        Inlines.OpusAssert(ret == silk_SUB_SAT32(a, b));
        return ret;
    }

    static long silk_SUB64(long a, long b) {
        long ret = a - b;
        Inlines.OpusAssert(ret == silk_SUB_SAT64(a, b));
        return ret;
    }

    static int silk_SAT8(int a) {
        return a > Byte.MAX_VALUE ? Byte.MAX_VALUE : ((a) < Byte.MIN_VALUE ? Byte.MIN_VALUE : (a));
    }

    static int silk_SAT16(int a) {
        return a > Short.MAX_VALUE ? Short.MAX_VALUE : ((a) < Short.MIN_VALUE ? Short.MIN_VALUE : (a));
    }

    static int silk_SAT32(long a) {
        return a > Integer.MAX_VALUE ? Integer.MAX_VALUE : ((a) < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) (a));
    }

    /// <summary>
    /// //////////////////
    /// </summary>
    /// <param name="a16"></param>
    /// <param name="b16"></param>
    /// <returns></returns>
    static short silk_ADD_SAT16(short a16, short b16) {
        short res = (short) silk_SAT16(silk_ADD32((int) (a16), (b16)));
        Inlines.OpusAssert(res == silk_SAT16((int) a16 + (int) b16));
        return res;
    }

    static int silk_ADD_SAT32(int a32, int b32) {
        int res = ((((long) (a32) + (long) (b32)) & 0x80000000) == 0
                ? ((((a32) & (b32)) & 0x80000000) != 0 ? Integer.MIN_VALUE : (a32) + (b32))
                : ((((a32) | (b32)) & 0x80000000) == 0 ? Integer.MAX_VALUE : (a32) + (b32)));
        Inlines.OpusAssert(res == silk_SAT32((long) a32 + (long) b32));
        return res;
    }

    static long silk_ADD_SAT64(long a64, long b64) {
        long res;
        res = (((a64 + b64) & Long.MIN_VALUE) == 0
                ? (((a64 & b64) & Long.MIN_VALUE) != 0 ? Long.MIN_VALUE : a64 + b64)
                : (((a64 | b64) & Long.MIN_VALUE) == 0 ? Long.MAX_VALUE : a64 + b64));
        return res;
    }

    static short silk_SUB_SAT16(short a16, short b16) {
        short res = (short) silk_SAT16(silk_SUB32((int) (a16), (b16)));
        Inlines.OpusAssert(res == silk_SAT16((int) a16 - (int) b16));
        return res;
    }

    static int silk_SUB_SAT32(int a32, int b32) {
        int res = ((((long) a32 - b32) & 0x80000000) == 0
                ? (((a32) & ((b32) ^ 0x80000000) & 0x80000000) != 0 ? Integer.MIN_VALUE : (a32) - (b32))
                : ((((a32) ^ 0x80000000) & (b32) & 0x80000000) != 0 ? Integer.MAX_VALUE : (a32) - (b32)));
        Inlines.OpusAssert(res == silk_SAT32((long) a32 - (long) b32));
        return res;
    }

    static long silk_SUB_SAT64(long a64, long b64) {
        long res;
        res = (((long) ((a64) - (b64)) & Long.MIN_VALUE) == 0
                ? (((long) (a64) & ((long) (b64) ^ Long.MIN_VALUE) & Long.MIN_VALUE) != 0 ? Long.MIN_VALUE : (a64) - (b64))
                : ((((long) (a64) ^ Long.MIN_VALUE) & (long) (b64) & Long.MIN_VALUE) != 0 ? Long.MAX_VALUE : (a64) - (b64)));
        return res;
    }

    ///* Saturation for positive input values */
    //#define silk_POS_SAT32(a)                   ((a) > int_MAX ? int_MAX : (a))
    /// <summary>
    /// Add with saturation for positive input values
    /// </summary>
    /// <param name="a"></param>
    /// <param name="b"></param>
    /// <returns></returns>
    static byte silk_ADD_POS_SAT8(byte a, byte b) {
        return (byte) ((((a + b) & 0x80) != 0) ? Byte.MAX_VALUE : (a + b));
    }

    /// <summary>
    /// Add with saturation for positive input values
    /// </summary>
    /// <param name="a"></param>
    /// <param name="b"></param>
    /// <returns></returns>
    static short silk_ADD_POS_SAT16(short a, short b) {
        return (short) ((((a + b) & 0x8000) != 0) ? Short.MAX_VALUE : (a + b));
    }

    /// <summary>
    /// Add with saturation for positive input values
    /// </summary>
    /// <param name="a"></param>
    /// <param name="b"></param>
    /// <returns></returns>
    static int silk_ADD_POS_SAT32(int a, int b) {
        return ((((a + b) & 0x80000000) != 0) ? Integer.MAX_VALUE : (a + b));
    }

    /// <summary>
    /// Add with saturation for positive input values
    /// </summary>
    /// <param name="a"></param>
    /// <param name="b"></param>
    /// <returns></returns>
    static long silk_ADD_POS_SAT64(long a, long b) {
        return ((((long) (a + b) & Long.MIN_VALUE) != 0) ? Long.MAX_VALUE : (a + b));
    }

    static byte silk_LSHIFT8(byte a, int shift) {
        byte ret = (byte) (a << shift);
        return ret;
    }

    static short silk_LSHIFT16(short a, int shift) {
        short ret = (short) (a << shift);
        return ret;
    }

    static int silk_LSHIFT32(int a, int shift) {
        int ret = a << shift;
        return ret;
    }

    static long silk_LSHIFT64(long a, int shift) {
        long ret = a << shift;
        return ret;
    }

    static int silk_LSHIFT(int a, int shift) {
        int ret = a << shift;
        return ret;
    }

    static int silk_LSHIFT_ovflw(int a, int shift) {
        return a << shift;
    }

    /// <summary>
    /// saturates before shifting
    /// </summary>
    /// <param name="a"></param>
    /// <param name="shift"></param>
    /// <returns></returns>
    static int silk_LSHIFT_SAT32(int a, int shift) {
        return (silk_LSHIFT32(silk_LIMIT((a), silk_RSHIFT32(Integer.MIN_VALUE, (shift)), silk_RSHIFT32(Integer.MAX_VALUE, (shift))), (shift)));
    }

    static byte silk_RSHIFT8(byte a, int shift) {
        return (byte) (a >> shift);
    }

    static short silk_RSHIFT16(short a, int shift) {
        return (short) (a >> shift);
    }

    static int silk_RSHIFT32(int a, int shift) {
        return a >> shift;
    }

    static int silk_RSHIFT(int a, int shift) {
        return a >> shift;
    }

    static long silk_RSHIFT64(long a, int shift) {
        return a >> shift;
    }

    static long silk_RSHIFT_uint(long a, int shift) {
        return CapToUInt32(a) >> shift;
    }

    static int silk_ADD_LSHIFT(int a, int b, int shift) {
        int ret = a + (b << shift);
        return ret;
        /* shift >= 0 */
    }

    static int silk_ADD_LSHIFT32(int a, int b, int shift) {
        int ret = a + (b << shift);
        return ret;
        /* shift >= 0 */
    }

    static int silk_ADD_RSHIFT(int a, int b, int shift) {
        int ret = a + (b >> shift);
        return ret;
        /* shift  > 0 */
    }

    static int silk_ADD_RSHIFT32(int a, int b, int shift) {
        int ret = a + (b >> shift);
        return ret;
        /* shift  > 0 */
    }

    static long silk_ADD_RSHIFT_uint(long a, long b, int shift) {
        long ret = CapToUInt32(a + (CapToUInt32(b) >> shift));
        return ret;
        /* shift  > 0 */
    }

    static int silk_SUB_LSHIFT32(int a, int b, int shift) {
        int ret;
        ret = a - (b << shift);
        return ret;
        /* shift >= 0 */
    }

    static int silk_SUB_RSHIFT32(int a, int b, int shift) {
        int ret;
        ret = a - (b >> shift);
        return ret;
        /* shift  > 0 */
    }

    static int silk_RSHIFT_ROUND(int a, int shift) {
        int ret;
        ret = shift == 1 ? (a >> 1) + (a & 1) : ((a >> (shift - 1)) + 1) >> 1;
        return ret;
    }

    static long silk_RSHIFT_ROUND64(long a, int shift) {
        long ret;
        ret = shift == 1 ? (a >> 1) + (a & 1) : ((a >> (shift - 1)) + 1) >> 1;
        return ret;
    }

    static int silk_min(int a, int b) {
        return ((a) < (b)) ? (a) : (b);
    }

    static int silk_max(int a, int b) {
        return ((a) > (b)) ? (a) : (b);
    }

    static float silk_min(float a, float b) {
        return ((a) < (b)) ? (a) : (b);
    }

    static float silk_max(float a, float b) {
        return ((a) > (b)) ? (a) : (b);
    }

    /// <summary>
    /// Macro to convert floating-point constants to fixed-point by applying a scalar factor
    /// Because of limitations of the JIT, this macro is actually evaluated at runtime and therefore should not be used if you want to maximize performance
    /// </summary>
    static int SILK_CONST(float number, int scale) {
        return ((int) ((number) * ((long) 1 << (scale)) + 0.5));
    }

    /* silk_min() versions with typecast in the function call */
    static int silk_min_int(int a, int b) {
        return (((a) < (b)) ? (a) : (b));
    }

    static short silk_min_16(short a, short b) {
        return (((a) < (b)) ? (a) : (b));
    }

    static int silk_min_32(int a, int b) {
        return (((a) < (b)) ? (a) : (b));
    }

    static long silk_min_64(long a, long b) {
        return (((a) < (b)) ? (a) : (b));
    }

    /* silk_min() versions with typecast in the function call */
    static int silk_max_int(int a, int b) {
        return (((a) > (b)) ? (a) : (b));
    }

    static short silk_max_16(short a, short b) {
        return (((a) > (b)) ? (a) : (b));
    }

    static int silk_max_32(int a, int b) {
        return (((a) > (b)) ? (a) : (b));
    }

    static long silk_max_64(long a, long b) {
        return (((a) > (b)) ? (a) : (b));
    }

    static float silk_LIMIT(float a, float limit1, float limit2) {
        return ((limit1) > (limit2) ? ((a) > (limit1) ? (limit1) : ((a) < (limit2) ? (limit2) : (a))) : ((a) > (limit2) ? (limit2) : ((a) < (limit1) ? (limit1) : (a))));
    }

    static int silk_LIMIT(int a, int limit1, int limit2) {
        return silk_LIMIT_32(a, limit1, limit2);
    }

    static int silk_LIMIT_int(int a, int limit1, int limit2) {
        return silk_LIMIT_32(a, limit1, limit2);
    }

    static short silk_LIMIT_16(short a, short limit1, short limit2) {
        return ((limit1) > (limit2) ? ((a) > (limit1) ? (limit1) : ((a) < (limit2) ? (limit2) : (a))) : ((a) > (limit2) ? (limit2) : ((a) < (limit1) ? (limit1) : (a))));
    }

    static int silk_LIMIT_32(int a, int limit1, int limit2) {
        return ((limit1) > (limit2) ? ((a) > (limit1) ? (limit1) : ((a) < (limit2) ? (limit2) : (a))) : ((a) > (limit2) ? (limit2) : ((a) < (limit1) ? (limit1) : (a))));
    }

    static int silk_abs(int a) {
        // Be careful, silk_abs returns wrong when input equals to silk_intXX_MIN
        return ((a) > 0) ? (a) : -(a);
    }

    static int silk_abs_int16(int a) {
        return (a ^ (a >> 15)) - (a >> 15);
    }

    static int silk_abs_int32(int a) {
        return (a ^ (a >> 31)) - (a >> 31);
    }

    static long silk_abs_int64(long a) {
        return ((a) > 0) ? (a) : -(a);
    }

    static long silk_sign(int a) {
        return (a) > 0 ? 1 : ((a) < 0 ? -1 : 0);
    }

    /// <summary>
    /// PSEUDO-RANDOM GENERATOR
    /// Make sure to store the result as the seed for the next call (also in between
    /// frames), otherwise result won't be random at all. When only using some of the
    /// bits, take the most significant bits by right-shifting.
    /// </summary>
    static int silk_RAND(int seed) {
        return silk_MLA_ovflw(907633515, seed, 196314165);
    }

    /// <summary>
    /// silk_SMMUL: Signed top word multiply.
    /// </summary>
    /// <param name="a32"></param>
    /// <param name="b32"></param>
    /// <returns></returns>
    static int silk_SMMUL(int a32, int b32) {
        return (int) silk_RSHIFT64(silk_SMULL((a32), (b32)), 32);
    }

    /* a32 + (b32 * (c32 >> 16)) >> 16 */
    static int silk_SMLAWT(int a32, int b32, int c32) {
        int ret = a32 + ((b32 >> 16) * (c32 >> 16)) + (((b32 & 0x0000FFFF) * ((c32 >> 16)) >> 16));
        return ret;
    }

    /// <summary>
    /// Divide two int32 values and return result as int32 in a given Q-domain
    /// </summary>
    /// <param name="a32">I    numerator (Q0)</param>
    /// <param name="b32">I    denominator (Q0)</param>
    /// <param name="Qres">I    Q-domain of result (>= 0)</param>
    /// <returns>O    returns a good approximation of "(a32 << Qres) / b32"</returns>
    static int silk_DIV32_varQ(int a32, int b32, int Qres) {
        int a_headrm, b_headrm, lshift;
        int b32_inv, a32_nrm, b32_nrm, result;

        Inlines.OpusAssert(b32 != 0);
        Inlines.OpusAssert(Qres >= 0);

        /* Compute number of bits head room and normalize inputs */
        a_headrm = silk_CLZ32(silk_abs(a32)) - 1;
        a32_nrm = silk_LSHIFT(a32, a_headrm);
        /* Q: a_headrm                  */
        b_headrm = silk_CLZ32(silk_abs(b32)) - 1;
        b32_nrm = silk_LSHIFT(b32, b_headrm);
        /* Q: b_headrm                  */

 /* Inverse of b32, with 14 bits of precision */
        b32_inv = silk_DIV32_16(Integer.MAX_VALUE >> 2, silk_RSHIFT(b32_nrm, 16));
        /* Q: 29 + 16 - b_headrm        */

 /* First approximation */
        result = silk_SMULWB(a32_nrm, b32_inv);
        /* Q: 29 + a_headrm - b_headrm  */

 /* Compute residual by subtracting product of denominator and first approximation */
 /* It's OK to overflow because the final value of a32_nrm should always be small */
        a32_nrm = silk_SUB32_ovflw(a32_nrm, silk_LSHIFT_ovflw(silk_SMMUL(b32_nrm, result), 3));
        /* Q: a_headrm   */

 /* Refinement */
        result = silk_SMLAWB(result, a32_nrm, b32_inv);
        /* Q: 29 + a_headrm - b_headrm  */

 /* Convert to Qres domain */
        lshift = 29 + a_headrm - b_headrm - Qres;
        if (lshift < 0) {
            return silk_LSHIFT_SAT32(result, -lshift);
        } else if (lshift < 32) {
            return silk_RSHIFT(result, lshift);
        } else {
            /* Avoid undefined result */
            return 0;
        }
    }

    /// <summary>
    /// Invert int32 value and return result as int32 in a given Q-domain
    /// </summary>
    /// <param name="b32">I    denominator (Q0)</param>
    /// <param name="Qres">I    Q-domain of result (> 0)</param>
    /// <returns>a good approximation of "(1 << Qres) / b32"</returns>
    static int silk_INVERSE32_varQ(int b32, int Qres) {
        int b_headrm, lshift;
        int b32_inv, b32_nrm, err_Q32, result;

        Inlines.OpusAssert(b32 != 0);
        Inlines.OpusAssert(Qres > 0);

        /* Compute number of bits head room and normalize input */
        b_headrm = silk_CLZ32(silk_abs(b32)) - 1;
        b32_nrm = silk_LSHIFT(b32, b_headrm);
        /* Q: b_headrm                */

 /* Inverse of b32, with 14 bits of precision */
        b32_inv = silk_DIV32_16(Integer.MAX_VALUE >> 2, (short) (silk_RSHIFT(b32_nrm, 16)));
        /* Q: 29 + 16 - b_headrm    */

 /* First approximation */
        result = silk_LSHIFT(b32_inv, 16);
        /* Q: 61 - b_headrm            */

 /* Compute residual by subtracting product of denominator and first approximation from one */
        err_Q32 = silk_LSHIFT(((int) 1 << 29) - silk_SMULWB(b32_nrm, b32_inv), 3);
        /* Q32                        */

 /* Refinement */
        result = silk_SMLAWW(result, err_Q32, b32_inv);
        /* Q: 61 - b_headrm            */

 /* Convert to Qres domain */
        lshift = 61 - b_headrm - Qres;
        if (lshift <= 0) {
            return silk_LSHIFT_SAT32(result, -lshift);
        } else if (lshift < 32) {
            return silk_RSHIFT(result, lshift);
        } else {
            /* Avoid undefined result */
            return 0;
        }
    }

    //////////////////////// from macros.h /////////////////////////////////////////////
    /// <summary>
    /// a32 + (b32 * (int)((short)(c32))) >> 16 output have to be 32bit int
    /// </summary>
    // fixme: This method should be as optimized as possible
    static int silk_SMLAWB(int a32, int b32, int c32) {
        //return (int)(a32 + ((b32 * (long)((short)c32)) >> 16));
        int ret;
        ret = a32 + silk_SMULWB(b32, c32);
        return ret;
    }

    ///* (a32 * (b32 >> 16)) >> 16 */
    static int silk_SMULWT(int a32, int b32) {
        return (((a32) >> 16) * ((b32) >> 16) + ((((a32) & 0x0000FFFF) * ((b32) >> 16)) >> 16));
    }

    ///* (int)((short)(a32)) * (b32 >> 16) */
    static int silk_SMULBT(int a32, int b32) {
        return ((int) ((short) (a32)) * ((b32) >> 16));
    }

    ///* a32 + (int)((short)(b32)) * (c32 >> 16) */
    static int silk_SMLABT(int a32, int b32, int c32) {
        return ((a32) + ((int) ((short) (b32))) * ((c32) >> 16));
    }

    ///* a64 + (b32 * c32) */
    static long silk_SMLAL(long a64, int b32, int c32) {
        return (silk_ADD64((a64), ((long) (b32) * (long) (c32))));
    }

    static int MatrixGetPointer(int row, int column, int N) {
        return (row * N) + column;
    }

    static int MatrixGet(int[] Matrix_base_adr, int row, int column, int N) {
        return Matrix_base_adr[((row) * (N)) + (column)];
    }

    static short MatrixGet(short[] Matrix_base_adr, int row, int column, int N) {
        return Matrix_base_adr[((row) * (N)) + (column)];
    }

    static PitchAnalysisCore.silk_pe_stage3_vals MatrixGet(PitchAnalysisCore.silk_pe_stage3_vals[] Matrix_base_adr, int row, int column, int N) {
        return Matrix_base_adr[((row) * (N)) + (column)];
    }

    static int MatrixGet(int[] Matrix_base_adr, int matrix_ptr, int row, int column, int N) {
        return Matrix_base_adr[matrix_ptr + (row * N) + column];
    }

    static short MatrixGet(short[] Matrix_base_adr, int matrix_ptr, int row, int column, int N) {
        return Matrix_base_adr[matrix_ptr + (row * N) + column];
    }

    static void MatrixSet(int[] Matrix_base_adr, int matrix_ptr, int row, int column, int N, int value) {
        Matrix_base_adr[matrix_ptr + (row * N) + column] = value;
    }

    static void MatrixSet(short[] Matrix_base_adr, int matrix_ptr, int row, int column, int N, short value) {
        Matrix_base_adr[matrix_ptr + (row * N) + column] = value;
    }

    static void MatrixSet(int[] Matrix_base_adr, int row, int column, int N, int value) {
        Matrix_base_adr[((row) * (N)) + (column)] = value;
    }

    static void MatrixSet(short[] Matrix_base_adr, int row, int column, int N, short value) {
        Matrix_base_adr[((row) * (N)) + (column)] = value;
    }

    /// <summary>
    /// (a32 * b32) >> 16
    /// </summary>
    static int silk_SMULWW(int a32, int b32) {
        //return CHOP32(((long)(a32) * (b32)) >> 16);
        return silk_MLA(silk_SMULWB((a32), (b32)), (a32), silk_RSHIFT_ROUND((b32), 16));
    }

    /// <summary>
    /// a32 + ((b32 * c32) >> 16)
    /// </summary>
    static int silk_SMLAWW(int a32, int b32, int c32) {
        //return CHOP32(((a32) + (((long)(b32) * (c32)) >> 16)));
        return silk_MLA(silk_SMLAWB((a32), (b32), (c32)), (b32), silk_RSHIFT_ROUND((c32), 16));
    }

    /* count leading zeros of opus_int64 */
    static int silk_CLZ64(long input) {
        int in_upper;

        in_upper = (int) silk_RSHIFT64(input, 32);
        if (in_upper == 0) {
            /* Search in the lower 32 bits */
            return 32 + silk_CLZ32(((int) input));
        } else {
            /* Search in the upper 32 bits */
            return silk_CLZ32(in_upper);
        }
    }

    static int silk_CLZ32(int in32) {
        return in32 == 0 ? 32 : 32 - EC_ILOG(in32);
    }

    /// <summary>
    /// Get number of leading zeros and fractional part (the bits right after the leading one)
    /// </summary>
    /// <param name="input">input</param>
    /// <param name="lz">number of leading zeros</param>
    /// <param name="frac_Q7">the 7 bits right after the leading one</param>
    static void silk_CLZ_FRAC(int input, BoxedValueInt lz, BoxedValueInt frac_Q7) {
        int lzeros = silk_CLZ32(input);

        lz.Val = lzeros;
        frac_Q7.Val = silk_ROR32(input, 24 - lzeros) & 0x7f;
    }

    /// <summary>
    /// Approximation of square root.
    /// Accuracy: +/- 10%  for output values > 15
    ///           +/- 2.5% for output values > 120
    /// </summary>
    /// <param name="x"></param>
    /// <returns></returns>
    static int silk_SQRT_APPROX(int x) {
        int y;
        int lz;
        int frac_Q7;

        if (x <= 0) {
            return 0;
        }

        BoxedValueInt boxed_lz = new BoxedValueInt(0);
        BoxedValueInt boxed_frac_Q7 = new BoxedValueInt(0);
        silk_CLZ_FRAC(x, boxed_lz, boxed_frac_Q7);
        lz = boxed_lz.Val;
        frac_Q7 = boxed_frac_Q7.Val;

        if ((lz & 1) != 0) {
            y = 32768;
        } else {
            y = 46214;        // 46214 = sqrt(2) * 32768
        }

        // get scaling right
        y >>= silk_RSHIFT(lz, 1);

        // increment using fractional part of input
        y = silk_SMLAWB(y, y, silk_SMULBB(213, frac_Q7));

        return y;
    }

    static int MUL32_FRAC_Q(int a32, int b32, int Q) {
        return ((int) (silk_RSHIFT_ROUND64(silk_SMULL(a32, b32), Q)));
    }

    /// <summary>
    /// Approximation of 128 * log2() (very close inverse of silk_log2lin())
    /// Convert input to a log scale
    /// </summary>
    /// <param name="inLin">(I) input in linear scale</param>
    /// <returns></returns>
    static int silk_lin2log(int inLin) {
        BoxedValueInt lz = new BoxedValueInt(0);
        BoxedValueInt frac_Q7 = new BoxedValueInt(0);

        silk_CLZ_FRAC(inLin, lz, frac_Q7);

        // Piece-wise parabolic approximation
        return silk_LSHIFT(31 - lz.Val, 7) + silk_SMLAWB(frac_Q7.Val, silk_MUL(frac_Q7.Val, 128 - frac_Q7.Val), 179);
    }

    /// <summary>
    /// Approximation of 2^() (very close inverse of silk_lin2log())
    /// Convert input to a linear scale
    /// </summary>
    /// <param name="inLog_Q7">input on log scale</param>
    /// <returns>Linearized value</returns>
    static int silk_log2lin(int inLog_Q7) {
        int output, frac_Q7;

        if (inLog_Q7 < 0) {
            return 0;
        } else if (inLog_Q7 >= 3967) {
            return Integer.MAX_VALUE;
        }

        output = silk_LSHIFT(1, silk_RSHIFT(inLog_Q7, 7));
        frac_Q7 = inLog_Q7 & 0x7F;

        if (inLog_Q7 < 2048) {
            /* Piece-wise parabolic approximation */
            output = silk_ADD_RSHIFT32(output, silk_MUL(output, silk_SMLAWB(frac_Q7, silk_SMULBB(frac_Q7, 128 - frac_Q7), -174)), 7);
        } else {
            /* Piece-wise parabolic approximation */
            output = silk_MLA(output, silk_RSHIFT(output, 7), silk_SMLAWB(frac_Q7, silk_SMULBB(frac_Q7, 128 - frac_Q7), -174));
        }

        return output;
    }

    /// <summary>
    /// Interpolate two vectors
    /// </summary>
    /// <param name="xi">(O) interpolated vector [MAX_LPC_ORDER]</param>
    /// <param name="x0">(I) first vector [MAX_LPC_ORDER]</param>
    /// <param name="x1">(I) second vector [MAX_LPC_ORDER]</param>
    /// <param name="ifact_Q2">(I) interp. factor, weight on 2nd vector</param>
    /// <param name="d">(I) number of parameters</param>
    static void silk_interpolate(
            short[] xi,
            short[] x0,
            short[] x1,
            int ifact_Q2,
            int d) {
        int i;

        Inlines.OpusAssert(ifact_Q2 >= 0);
        Inlines.OpusAssert(ifact_Q2 <= 4);

        for (i = 0; i < d; i++) {
            xi[i] = (short) silk_ADD_RSHIFT(x0[i], silk_SMULBB(x1[i] - x0[i], ifact_Q2), 2);
        }
    }

    /// <summary>
    /// Inner product with bit-shift
    /// </summary>
    /// <param name="inVec1">I input vector 1</param>
    /// <param name="inVec2">I input vector 2</param>
    /// <param name="scale">I number of bits to shift</param>
    /// <param name="len">I vector lengths</param>
    /// <returns></returns>
    static int silk_inner_prod_aligned_scale(
            short[] inVec1,
            short[] inVec2,
            int scale,
            int len) {
        int i, sum = 0;
        for (i = 0; i < len; i++) {
            sum = silk_ADD_RSHIFT32(sum, silk_SMULBB(inVec1[i], inVec2[i]), scale);
        }

        return sum;
    }

    /* Copy and multiply a vector by a constant */
    static void silk_scale_copy_vector16(
            short[] data_out,
            int data_out_ptr,
            short[] data_in,
            int data_in_ptr,
            int gain_Q16, /* I    Gain in Q16                                                 */
            int dataSize /* I    Length                                                      */
    ) {
        for (int i = 0; i < dataSize; i++) {
            data_out[data_out_ptr + i] = (short) (silk_SMULWB(gain_Q16, data_in[data_in_ptr + i]));
        }
    }

    /* Multiply a vector by a constant */
    static void silk_scale_vector32_Q26_lshift_18(
            int[] data1, /* I/O  Q0/Q18                                                      */
            int data1_ptr,
            int gain_Q26, /* I    Q26                                                         */
            int dataSize /* I    length                                                      */
    ) {
        for (int i = data1_ptr; i < data1_ptr + dataSize; i++) {
            data1[i] = (int) (silk_RSHIFT64(silk_SMULL(data1[i], gain_Q26), 8));
            /* OUTPUT: Q18 */
        }
    }

    /* sum = for(i=0;i<len;i++)inVec1[i]*inVec2[i];      ---        inner product   */
    static int silk_inner_prod(
            short[] inVec1, /*    I input vector 1                                              */
            int inVec1_ptr,
            short[] inVec2, /*    I input vector 2                                              */
            int inVec2_ptr,
            int len /*    I vector lengths                                              */
    ) {
        int i;
        int xy = 0;
        for (i = 0; i < len; i++) {
            xy = Inlines.MAC16_16(xy, inVec1[inVec1_ptr + i], inVec2[inVec2_ptr + i]);
        }
        return xy;
    }

    static int silk_inner_prod_self(
            short[] inVec, /*    I input vector 1 (will be crossed with itself)                                             */
            int inVec_ptr,
            int len /*    I vector lengths                                              */
    ) {
        int i;
        int xy = 0;
        for (i = inVec_ptr; i < inVec_ptr + len; i++) {
            xy = Inlines.MAC16_16(xy, inVec[i], inVec[i]);
        }
        return xy;
    }

    static long silk_inner_prod16_aligned_64(
            short[] inVec1, /*    I input vector 1                                              */
            int inVec1_ptr,
            short[] inVec2, /*    I input vector 2                                              */
            int inVec2_ptr,
            int len /*    I vector lengths                                              */
    ) {
        int i;
        long sum = 0;
        for (i = 0; i < len; i++) {
            sum = silk_SMLALBB(sum, inVec1[inVec1_ptr + i], inVec2[inVec2_ptr + i]);
        }
        return sum;
    }

    /// <summary>
    /// returns the value that has fewer higher-order bits, ignoring sign bit (? I think?)
    /// </summary>
    /// <param name="a"></param>
    /// <param name="b"></param>
    /// <returns></returns>
    static long EC_MINI(long a, long b) {
        return (a + ((b - a) & ((b < a) ? 0xFFFFFFFF : 0)));
    }

    static int EC_ILOG(long x) {
        if (x == 0) {
            return 1;
        }
        x |= (x >> 1);
        x |= (x >> 2);
        x |= (x >> 4);
        x |= (x >> 8);
        x |= (x >> 16);
        long y = x - ((x >> 1) & 0x55555555);
        y = (((y >> 2) & 0x33333333) + (y & 0x33333333));
        y = (((y >> 4) + y) & 0x0f0f0f0f);
        y += (y >> 8);
        y += (y >> 16);
        y = (y & 0x0000003f);
        return (int) y;
    }

    static int abs(int a) {
        if (a < 0) {
            return 0 - a;
        }
        return a;
    }

    static int SignedByteToUnsignedInt(byte b) {
        return (b & 0xFF);
    }
}
