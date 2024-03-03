import argparse
import logging
import sys
from logging.handlers import RotatingFileHandler
from subprocess import Popen, PIPE

import matplotlib.pyplot as plt

logger = logging.getLogger("statistik_logger")


def style_plot(commits_num: int):
    logger.debug("styling plot")
    # Set labels and title
    plt.xlabel('hour')
    plt.ylabel('weekday')
    plt.title(f'Tobias Hernandez Perez: {commits_num} commits')

    # Spacing
    plt.xlim(-2.5, 25 * 1.1)
    plt.ylim(-0.5, 6.5)

    # Markierungen
    plt.xticks([0, 5, 10, 15, 20, 25])
    plt.yticks([1, 2, 3, 4, 5, 6, 0])
    plt.yticks([1, 2, 3, 4, 5, 6, 0],
               ["mon", "tue", "wed", "thu", "fri", "sat", "sun"])

    # Achsen
    ax = plt.gca()

    # Grid
    plt.grid()
    plt.axvline(x=8, color='gray', linestyle='-', linewidth=1, label='Extra Gridline')
    plt.axvline(x=18, color='gray', linestyle='-', linewidth=1, label='Extra Gridline')

    # Bessere Sichtbarkeit der Ticks
    for label in ax.get_xticklabels() + ax.get_yticklabels():
        label.set_fontsize(16)
        label.set_bbox(dict(facecolor='white', edgecolor='None', alpha=0.65))

    ax.set_axisbelow(True)


def create_git_scatter(folder: str):
    logger.debug(f"creating scatter of {folder}")
    git_log = ["git", "-C", folder, "log", "--pretty=format:%ad", "--date=format-local:%w %H"]
    process = Popen(git_log, stdout=PIPE, stderr=PIPE, text=True)
    out, err = process.communicate()
    log_entries = out.splitlines()

    commits_by_weekday_hour = {}
    commits_num = 0

    for entry in log_entries:
        logger.debug(f"reading entry: {entry}")
        weekday, hour = entry.split()
        key = (int(weekday), int(hour))
        commits_by_weekday_hour[key] = commits_by_weekday_hour.get(key, 0) + 10
        commits_num += 1

    weekdays, hours = zip(*commits_by_weekday_hour.keys())
    counts = list(commits_by_weekday_hour.values())

    plt.scatter(hours, weekdays, s=counts, c='blue', alpha=0.5)
    style_plot(commits_num)
    # plt.savefig("statistik_hernandezperez.png", dpi=72)
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

    argparser = argparse.ArgumentParser()
    argparser.add_argument("folder")
    argparser.add_argument('-v', "--verbosity", help="increase output verbosity", action="store_true")
    argparser.add_argument("-q", "--quiet", help="decrease output verbosity", action="store_true")

    args = argparser.parse_args()

    if args.verbosity:
        print("verbosity turned on")
        logger.setLevel(logging.DEBUG)
    elif args.quiet:
        logger.setLevel(logging.NOTSET)

    if args.folder:
        create_git_scatter(args.folder)


if __name__ == '__main__':
    parse_args()
