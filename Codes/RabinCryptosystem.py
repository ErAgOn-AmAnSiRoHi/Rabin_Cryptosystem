import random
from sympy import isprime

def encrypt(m, n):
    # Calculate the square of the decimal value modulo n
    c = (m * m) % n
    return c

def modular_exponentiation(k, b, m):
    i = 0
    a = 1
    binary = []
    while k > 0:
        binary.append(k % 2)
        k = k // 2
        i += 1
    for j in range(i):
        if binary[j] == 1:
            a = (a * b) % m
        b = (b * b) % m
    return a

# def extended_euclid(a, b):
#     s = 0
#     old_s = 1
#     t = 1
#     old_t = 0
#     r = b
#     old_r = a
#     while r != 0:
#         quotient = old_r // r
#         temp = r
#         r = old_r - quotient * r
#         old_r = temp
#         temp = s
#         s = old_s - quotient * s
#         old_s = temp
#         temp = t
#         t = old_t - quotient * t
#         old_t = temp
#     return old_r, old_s, old_t

def extended_euclid(a,b):
    if b == 0:
        return a,1,0
    else:
        d, x, y = extended_euclid(b, a%b)
        return d, y, x - (a // b) * y

def decrypt(c, p, q):
    n = p * q
    p1 = modular_exponentiation((p + 1) // 4, c, p)
    p2 = p - p1
    q1 = modular_exponentiation((q + 1) // 4, c, q)
    q2 = q - q1
    _, y_p, y_q = extended_euclid(p, q)
    d1 = (y_p * p * q1 + y_q * q * p1) % n
    d2 = (y_p * p * q2 + y_q * q * p1) % n
    d3 = (y_p * p * q1 + y_q * q * p2) % n
    d4 = (y_p * p * q2 + y_q * q * p2) % n

    return [d1, d2, d3, d4]

def main():
    e = [0] * 100000
    d = [0] * 100000

    # Generate two random prime numbers which are congruent to 3 modulo 4
    p = random.randint(2**15, 2**16 - 1)
    while not (isprime(p) and p % 4 == 3):
        p = random.randint(2**15, 2**16 - 1)

    q = random.randint(2**15, 2**16 - 1)
    while not (isprime(q) and q % 4 == 3 and q != p):
        q = random.randint(2**15, 2**16 - 1)

    n = p * q

    string = str(input("Enter the Plain Text: "))
    print()

    # Convert each character in the string to its ASCII value
    bytes = [ord(c) for c in string]

    # Pass each ASCII value to encryption function
    for i in range(len(bytes)):
        e[i] = encrypt(bytes[i], n)

    # Print the encrypted message
    print("Encrypted Message: ", end="")
    for i in range(len(bytes)):
        print(e[i], end = "")
    print()

    # Pass each element of the encrypted message to decryption function
    for i in range(len(bytes)):
        d1 = decrypt(e[i], p, q)
        # print d1[] to see the four possible decrypted values
        for d_val in d1:
            if 0 <= d_val <= 127:
                d[i] = d_val
                break

    # Print the decrypted message
    print("\nDecrypted Message: ", end="")
    for i in range(len(bytes)):
        print(chr(d[i]), end="")
    print()

if __name__ == "__main__":
    main()
