def loop (host, port, f):
    from hacksign import Sign
    
    s = Sign (host, port)
    try:
        while True:
            s.send_frame (f ())
    except KeyboardInterrupt:
        # Blank the channel when stopped.
        from hacksign import Frame
        s.send_frame (Frame ())
