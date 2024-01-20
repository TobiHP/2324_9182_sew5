import argparse
import logging
import sys
from datetime import datetime
from logging.handlers import RotatingFileHandler
from subprocess import Popen, PIPE

import matplotlib.pyplot as plt

logger = logging.getLogger("statistik_logger")


def create_git_scatter():
    git_log = ["git", "log", "--pretty=format:%h|%an|%ad|%s", "--date=format-local:%Y-%m-%d %H:%M:%S"]
    process = Popen(git_log, stdout=PIPE, stderr=PIPE, text=True)
    out, err = process.communicate()

    log_entries = out.splitlines()

    commits_by_weekday_hour = {}

    for entry in log_entries:
        entry_components = entry.split('|')

        commit_date_time = entry_components[2]
        commit_datetime = datetime.strptime(commit_date_time, "%Y-%m-%d %H:%M:%S")

        weekday = commit_datetime.weekday()
        hour = commit_datetime.hour

        key = (weekday, hour)
        commits_by_weekday_hour[key] = commits_by_weekday_hour.get(key, 0) + 10

    weekdays, hours = zip(*commits_by_weekday_hour.keys())
    counts = list(commits_by_weekday_hour.values())

    plt.scatter(hours, weekdays, s=counts, c='blue', alpha=0.5)
    plt.show()


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
    create_git_scatter()
    # print(out)
    # parse_args()
