import argparse
import logging
import sys
from logging.handlers import RotatingFileHandler

import matplotlib.pyplot as plt

logger = logging.getLogger("sierpinski_logger")


def style_plot(name_color):
    """
    Add custom styling to the plot
    """
    logger.debug("styling plot")
    if name_color:
        plt.title(f'Tobias Hernandez Perez', color=name_color)
    else:
        plt.title(f'Tobias Hernandez Perez')


color = "#000000"


def draw_sierpinski_triangle(min_length: float, max_depth: int, left=(-7.5, 2.5), right=(-2.5, 2.5), top=(-5, 7.5),
                             depth=0):
    """
    Creates a scatter plot containing the number of commits per weekday & hour
    """
    if type(left) is str:
        left = left[1:-1]
        left = float(left.split(",")[0]), float(left.split(",")[1])
    if type(right) is str:
        right = right[1:-1]
        right = float(right.split(",")[0]), float(right.split(",")[1])
    if type(top) is str:
        top = top[1:-1]
        top = float(top.split(",")[0]), float(top.split(",")[1])

    if max_depth:
        if depth >= int(max_depth):
            return
    else:
        if float(min_length) >= right[0] - left[0]:
            return

    # links, rechts, spitze, links
    plt.plot((left[0], right[0], top[0], left[0]), (left[1], right[1], top[1], left[1]), color=color)

    # links unten
    draw_sierpinski_triangle(
        min_length=min_length,
        max_depth=max_depth,
        left=(left[0], left[1]),
        right=calc_midpoint(left[0], right[0], left[1], right[1]),
        top=calc_midpoint(left[0], top[0], left[1], top[1]),
        depth=depth + 1,
    )

    # rechts unten
    draw_sierpinski_triangle(
        min_length=min_length,
        max_depth=max_depth,
        left=calc_midpoint(left[0], right[0], left[1], right[1]),
        right=(right[0], right[1]),
        top=calc_midpoint(right[0], top[0], right[1], top[1]),
        depth=depth + 1
    )

    # oben
    draw_sierpinski_triangle(
        min_length=min_length,
        max_depth=max_depth,
        left=calc_midpoint(left[0], top[0], left[1], top[1]),
        right=calc_midpoint(right[0], top[0], right[1], top[1]),
        top=(top[0], top[1]),
        depth=depth + 1
    )

    # todo fill hinzufuegen
    # plt.fill((-7.5, -2.5, -5, -7.5), (2.5, 2.5, 7.5, 2.5), color='#ffffff')


def calc_midpoint(x1: float, x2: float, y1: float, y2: float):
    return (x1 + x2) / 2, (y1 + y2) / 2


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
    argparser.add_argument("filename", help="name of the output file")
    argparser.add_argument("-s", "--size", help="dimensions of the triangle e.g. 800x600 (length/width)")
    argparser.add_argument("-l", "--left", help="coordinates of the left point of the first triangle",
                           default="-7.5,2.5")
    argparser.add_argument("-r", "--right", help="coordinates of the right point of the first triangle",
                           default="-2.5,2.5")
    argparser.add_argument("-t", "--top", help="coordinates of the top point of the first triangle", default="-5,7.5")

    argparser.add_argument("-n", "--no_axes", help="hide the axes of the plot", action='store_true')
    argparser.add_argument("-c", "--color", help="set the color of the triangle")
    argparser.add_argument("-b", "--background", help="set the background color")
    argparser.add_argument("-m", "--name_color", help="set the color of the name")

    group1 = argparser.add_mutually_exclusive_group()
    group1.add_argument("-p", "--min_len", help="minimum length of an edge in pixel")
    group1.add_argument("-d", "--max_dep", help="maximum recursion depth")

    group2 = argparser.add_mutually_exclusive_group()
    group2.add_argument('-v', "--verbosity", help="increase output verbosity", action="store_true")
    group2.add_argument("-q", "--quiet", help="decrease output verbosity", action="store_true")

    args = argparser.parse_args()

    if args.verbosity:
        print("verbosity turned on")
        logger.setLevel(logging.DEBUG)
    elif args.quiet:
        logger.setLevel(logging.NOTSET)

    if args.size:
        size = args.size
        size = int(size.split("x")[0]), int(size.split("x")[1])
        plt.figure(figsize=size)

    if args.background:
        ax = plt.gca()
        ax.set_facecolor((1.0, 0.47, 0.42))

    if args.color:
        global color
        color = args.color

    if args.no_axes:
        plt.axis('off')

    if args.min_len or args.max_dep:
        draw_sierpinski_triangle(min_length=args.min_len, left=args.left, right=args.right, top=args.top,
                                 max_depth=args.max_dep)
        style_plot(args.name_color)

        # if args.filename:
        #     plt.savefig(args.filename, dpi=72)
        # else:
        plt.show()


if __name__ == '__main__':
    parse_args()
