import argparse
import logging
import sys
from logging.handlers import RotatingFileHandler
from subprocess import Popen, PIPE

logger = logging.getLogger("statistik_logger")


def create_logger():
    """
    creates the variables necessary for logging
    :return:
    """
    rot_file_handler = RotatingFileHandler('create_user.log', maxBytes=10_000, backupCount=5)
    stream_handler = logging.StreamHandler(sys.stdout)
    logger.addHandler(rot_file_handler)
    logger.addHandler(stream_handler)


def parse_args():
    """
    The necessary commands to make the command line tools usable
    :return:
    """
    create_logger()

    parser = argparse.ArgumentParser()
    parser.add_argument("folder")
    parser.add_argument('-v', "--verbosity", help="increase output verbosity", action="store_true")
    parser.add_argument("-q", "--quiet", help="decrease output verbosity", action="store_true")

    args = parser.parse_args()

    if args.verbosity:
        print("verbosity turned on")
        logger.setLevel(logging.DEBUG)
    elif args.quiet:
        logger.setLevel(logging.NOTSET)

    if args.folder:
        pass


if __name__ == '__main__':
    git_log = ["git", "log"]
    process = Popen(git_log, stdout=PIPE, stderr=PIPE, text=True)
    out, err = process.communicate()

    print(out)
    # parse_args()
