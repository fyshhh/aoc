#include <string>
#include <vector>

using namespace std;

bool is_digit(char c)
{
    return c >= '0' && c <= '9';
}

vector<string> split(string in, string delim)
{
    vector<string> out;
    int start = 0, end, l = delim.length();

    while ((end = in.find(delim, start)) != string::npos)
    {
        out.push_back(in.substr(start, end - start));
        start = end + l;
    }
    out.push_back(in.substr(start));

    return out;
}

vector<string> split_multi(string in, string delim)
{
    vector<string> out;
    int start = 0, end, l = delim.length();

    while ((end = in.find(delim, start)) != string::npos)
    {
        if (start != end)
            out.push_back(in.substr(start, end - start));
        start = end + l;
    }
    if (in.substr(start).length() != 0)
        out.push_back(in.substr(start));

    return out;
}
