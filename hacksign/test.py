import unittest

class UtilityTests (unittest.TestCase):
    def test_byteify (self):
        import hacksign
        
        self.assertEqual ("\x00\x01\x03", hacksign.byteify ([0, 1, 3]))

class SignTests (unittest.TestCase):
    # FIXME this test is way too long and complicated. Mocking framework
    # would help a lot, maybe. It also relies heavily on the way in which
    # sign.send_frame actually runs, which isn't (yet) documented in the
    # sign class' docstrings.
    def test_one_frame (self):
        # Using a local variable for this would run afoul of Python's
        # variable declaration rules....
        class buffer_holder:
            actual_buffer = None
            expected_buffer = "NOT A REAL BUFFER"
            
            def check (holder_self):
                self.assertEqual (holder_self.expected_buffer,
                                  holder_self.actual_buffer)

        holder = buffer_holder ()
        
        import hacksign
        
        class mocket:
            def sendto (udp_mocket_self, buffer, flags, destination):
                self.assertEqual (0, flags)
                self.assertEqual (("ip address", "port"), destination)
                
                # ... here.
                holder.actual_buffer = buffer
        
        def encoder_mock (source):
            self.assertEqual ("H" + "FLATTENED", source)
            return "NOT A REAL BUFFER"
        
        class frame_mock:
            def flatten (frame_mock_self):
                return "FLATTENED"
        
        sign = hacksign.Sign("ip address", "port", socket=mocket)
        sign._encode_body = encoder_mock
        sign.header = "H"
        sign.send_frame (frame_mock ())
        
        holder.check ()

class FrameTests (unittest.TestCase):
    def test_flatten (self):
        import hacksign
        
        # Image:
        # [ * ]
        # [* *]
        frame = hacksign.Frame (width=3, height=2)
        frame.set_pixel (1, 0, 255)
        frame.set_pixel (0, 1, 254)
        frame.set_pixel (2, 1, 253)
        
        self.assertEqual ([0, 255, 0, 254, 0, 253], frame.flatten ())

def for_class (case):
    loader = unittest.TestLoader()
    return loader.loadTestsFromTestCase(case)

testsuite = unittest.TestSuite (map (for_class, [
        UtilityTests,
        SignTests,
        FrameTests
    ]))