from setuptools import setup, find_packages

setup(
    name='XtQuantClient',
    version='0.2',
    packages=find_packages(),
    install_requires=[
        'requests',
        'pytz',
    ],
    python_requires='>=3.6',
)
