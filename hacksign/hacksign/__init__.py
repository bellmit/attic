def byteify(l):
    """Converts a list of numbers into an unsigned byte string using pack."""
    import struct
    
    return "".join(struct.pack('B', x) for x in l)

def udp_socket():
    """Creates a new UDP socket."""
    import socket
    
    return socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

class Sign:
    """Drives a single hacklab sign layer."""
    
    def __init__(self, host, port, socket=udp_socket):        
        self.host = host
        self.port = port
        
        self.socket = socket()
    
    def send_frame(self, image):
        flatImage = image.flatten()
        
        message = self.header + flatImage
        buffer = self._encode_body(message)
                
        self.socket.sendto(buffer, 0, (self.host, self.port))
    
    def _encode_body(self, message):
        return byteify(message)
    
    header = [0x23, # Magic number 1
              0x54, # Magic number 2
              0x26, # Magic number 3
              0x66, # Magic number 4
              0,    # Height MSB
              32,   # Height LSB
              0,    # Width MSB
              96,   # Height MSB
              0x00, 0x01, # Channel count(1)
              0x00, 0xFF] # Max value per pixel(0xFF)

class Frame:
    def __init__(self, width=96, height=32, fill=0):
        self.width = width
        self.height = height
        self.image = []
        for y in range(0, height):
            self.image = self.image + [[fill] * width]
    
    def set_pixel(self, x, y, value=1):
        self.image[y][x] = value
    
    def flatten(self):
        flatImage = []
        for raster in self.image:
            flatImage = flatImage + raster
        return flatImage
