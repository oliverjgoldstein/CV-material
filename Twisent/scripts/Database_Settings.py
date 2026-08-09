import os


def required_env(name):
    value = os.environ.get(name)
    if not value:
        raise RuntimeError("Missing required environment variable: " + name)
    return value


dbn = os.environ.get("TWISENT_DB_DRIVER", "mysql")
db = os.environ.get("TWISENT_DB_NAME", "dashbrd")
user = required_env("TWISENT_DB_USER")
pw = required_env("TWISENT_DB_PASSWORD")
