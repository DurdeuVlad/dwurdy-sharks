import socket, struct, sys

def rcon(host, port, password, commands):
    s = socket.create_connection((host, port), timeout=10)
    def send(t, payload):
        data = struct.pack("<ii", 1, t) + payload.encode() + b"\x00\x00"
        s.sendall(struct.pack("<i", len(data)) + data)
        ln = struct.unpack("<i", s.recv(4))[0]
        return s.recv(ln)[:-2].decode("utf-8", "replace")
    auth = send(3, password)
    for cmd in commands:
        out = send(2, cmd)
        print(f"> {cmd}\n{out}")
    s.close()

if __name__ == "__main__":
    rcon("127.0.0.1", 25575, "dwurdytest", sys.argv[1:])
