struct Node
    prnt::Union{Nothing, Node}
    chld::Vector{Union{Char, Node}}
    Node() = new(nothing, Vector{Union{Char, Node}}(undef, 0))      # for the root node
    Node(prnt::Node) = new(prnt, Vector{Union{Char, Node}}(undef, 0))
end

function parseinp(str)
    return str |> collect |> v -> filter(c -> c != ' ', v) |> parseseq
end

function parseinput()
    return readlines("y2020/day18/input.in") .|> collect .|> v -> filter(c -> c != ' ', v) |> parseseq
end

function parseseq(inp::Vector{Char})

    root = Node()
    curr = root

    for str in inp
        if str == '('
            subn = Node(curr)
            push!(curr.chld, subn)
            curr = subn
        elseif str == ')'
            curr = curr.prnt
        else
            push!(curr.chld, str)
        end
    end

    return root

end

function evalnode(c::Char, addfirst::Bool = false)
    return parse(Int, c)
end

function evalnode(vec::Vector, addfirst::Bool = false)
    if addfirst
        tmp = [evalnode(vec[1], true)]
        ptr = 1
        for i in 2:2:length(vec)
            if vec[i] == '+'
                tmp[ptr] += evalnode(vec[i + 1], true)
            else
                ptr += 1
                push!(tmp, evalnode(vec[i + 1], true))
            end
        end
        return prod(tmp)
    else
        val = evalnode(vec[1])
        for i in 2:2:length(vec)
            val = vec[i] == '+' ? val + evalnode(vec[i + 1]) : val * evalnode(vec[i + 1])
        end
        return val
    end
end

function evalnode(node::Node, addfirst::Bool = false)
    return evalnode(node.chld, addfirst)
end


function partone(inp)
    (inp .|> evalnode) |> sum |> println
end

function parttwo(inp)
    (inp .|> v -> evalnode(v, true)) |> sum |> println
end

inp = parseinput()
partone(inp)
parttwo(inp)
