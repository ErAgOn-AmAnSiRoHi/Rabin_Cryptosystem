
# include <iostream>
# include <string>
# include <ctime>
# include <cstdlib>
# include <cmath>
# include <vector>

using namespace std;

long  encrypt(long  m, long  n) {
    // Calculate the square of the decimal value modulo n
    long  c = (m * m) % n;
    return c;
}

long  modularExponentiation(long  k, long  b, long  m) {
    long  i = 0;
    long  a = 1;
    long  binary[100000];
    while (k > 0) {
        binary[i] = k % 2;
        k = k / 2;
        i++;
    }
    for (int j = 0; j < i; j++) {
        if (binary[j] == 1) {
            a = (a * b) % m;
            b = (b * b) % m;
        }
        else{
            b = (b * b) % m;
        }
    }
    return a;
}

long  modulo(long  a, long  b) {
    if (a >= 0)
        return a % b;
    else
        return b - abs(a%b) % b;
}

long * extended_euclid(long  a, long  b) {
    long  s = 0;
    long  old_s = 1;
    long  t = 1;
    long  old_t = 0;
    long  r = b;
    long  old_r = a;
    long  quotient, temp;
    while (r != 0) {
        quotient = old_r / r;
        temp = r;
        r = old_r - quotient * r;
        old_r = temp;
        temp = s;
        s = old_s - quotient * s;
        old_s = temp;
        temp = t;
        t = old_t - quotient * t;
        old_t = temp;
    }
    long * ans = new long [3];
    ans[0] = old_r;
    ans[1] = old_s;
    ans[2] = old_t;
    return ans;
}

long * decrypt(long  c, long  p, long  q) {
    long  n = p * q;
    long  p1 = modularExponentiation((p + 1) / 4, c, p);
    long  p2 = p - p1;
    long  q1 = modularExponentiation((q + 1) / 4, c, q);
    long  q2 = q - q1;
    long * ext = extended_euclid(p, q);
    long  y_p = ext[1];
    long  y_q = ext[2];
    long  d1 = (y_p * p * q1 + y_q * q * p1) % n;
    long  d2 = (y_p*p*q2 + y_q*q*p1) % n;
    long  d3 = (y_p * p * q1 + y_q * q * p2) % n;
    long  d4 = (y_p*p*q2 + y_q*q*p2) % n;
    // Print d1, d2, d3, d4 to see the four possible decrypted values
    // cout << "d1, d2, d3, d4: " << endl;
    // cout << d1 << " " << d2 << " " << d3 << " " << d4 << endl;
    long * ans = new long [4];
    ans[0] = d1;
    ans[1] = d2;
    ans[2] = d3;
    ans[3] = d4;
    return ans;
}

int main() {
    vector <long > e(100000);
    vector <long > d(100000);

    // Generate two random prime numbers which are congruent to 3 modulo 4

    // Take user input for value of p which is prime and congruent to 3 modulo 4
    cout<<endl;
    cout << "Enter a prime number p which is congruent to 3 modulo 4: ";
    long  p;
    cin >> p;

    cout << "Enter a prime number q which is congruent to 3 modulo 4: ";
    long  q;
    cin >> q;

    long  n = p * q;
    cout<<endl;
    // Write the message to be encrypted
    string str;
    cout << "Message: ";
    str = "Submission for Rabin Cryptosystem !!!";
    cout << str;
     
    // Leave a line
    cout<<endl;

    // Convert each character in the string to its ASCII value
    int bytes[str.length()];
    for (int i = 0; i < str.length(); i++) {
        bytes[i] = (int)str[i];
    }

    // Pass each ASCII value to encryption function

    for (int i = 0; i < str.length(); i++) {
        e[i] = encrypt(bytes[i], n);
    }

    // Print the encrypted message
    cout<<endl;
    cout << "Encrypted Message: ";
    for (int i = 0; i < str.length(); i++) {
        cout << e[i];
    }

    // Pass each element of the encrypted message to decryption function. 
    long  i = 0;
    string decrypted = "";
    cout<<endl;
    for (int i = 0; i < str.length(); i++) {
        long * d = decrypt(e[i], p, q);
        // Check which of the four possible decrypted values is the correct one by checking if the value lies between 0 and 127
        for (int j = 0; j < 4; j++) {
            if (d[j] >= 0 && d[j] <= 127) {
                decrypted += (char)d[j];
            }
        }


    }
    cout<<endl;
    // Print the decrypted message
    cout << "Decrypted Message: " << decrypted << endl;
    cout<<endl;
}
