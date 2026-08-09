def convert(s):
    if isinstance(s, bool):
        return s
    elif isinstance(s, str):
        if s.lower() == 'true':
            return True
        elif s.lower() == 'false':
            return False
        else:
            raise ValueError
    else:
        print 'Str To Bool Error.'
        raise ValueError