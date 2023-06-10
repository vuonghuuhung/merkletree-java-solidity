import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MerkleTree {
    private List<String> leaves;
    private String root;
    private List<List<String>> tree;

    public MerkleTree(List<String> documentList, String option) {
        this.leaves = generateLeaves(documentList);
        this.tree = buildTree(leaves);
        this.root = tree.get(tree.size() - 1).get(0);
    }

    public MerkleTree(List<byte[]> documentList) {
        List<String> stringList = convertByteArrayToStringList(documentList);
        this.leaves = generateLeaves(stringList);
        this.tree = buildTree(leaves);
        this.root = tree.get(tree.size() - 1).get(0);
    }

    private List<String> convertByteArrayToStringList(List<byte[]> byteArray) {
        List<String> stringList = new ArrayList<>();
        for (byte[] bytes : byteArray) {
            String hexString = Hex.toHexString(bytes);
            stringList.add(hexString);
        }
        return stringList;
    }

    public List<String> getLeaves() {
        return leaves;
    }

    public String getRoot() {
        return root;
    }

    public List<String> getProof(String leaf) {
        // need for verifying
        return null;
    }


    private List<String> generateLeaves(List<String> documentList) {
        List<String> leaves = new ArrayList<>();
        for (String document : documentList) {
            String hash = Hash.sha3(document);
            leaves.add(hash);
        }

        leaves.sort(new HexStringComparator());

        return leaves;
    }

    private List<List<String>> buildTree(List<String> leaves) {
        List<List<String>> tree = new ArrayList<>();
        tree.add(leaves);

        while (tree.get(tree.size() - 1).size() > 1) {
            List<String> level = new ArrayList<>();

            List<String> currentLevel = tree.get(tree.size() - 1);
            for (int i = 0; i < currentLevel.size(); i += 2) {
                String left = currentLevel.get(i);
                String right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : "";

                String combinedHash;
                if (left.equals("")) {
                    combinedHash = right;
                } else if (right.equals("")) {
                    combinedHash = left;
                } else if (left.compareTo(right) <= 0) {
                    combinedHash = Hash.sha3(stripHexPrefix(left) + stripHexPrefix(right));
                } else {
                    combinedHash = Hash.sha3(stripHexPrefix(right) + stripHexPrefix(left));
                }

                level.add(combinedHash);
            }

            tree.add(level);
        }

        return tree;
    }

    private String stripHexPrefix(String hexString) {
        return Numeric.cleanHexPrefix(hexString);
    }

    public static String keccak256(String data) {
        return Hash.sha3(data);
    }

    private class HexStringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            String strippedS1 = stripHexPrefix(s1);
            String strippedS2 = stripHexPrefix(s2);
            return strippedS1.compareTo(strippedS2);
        }
    }

    public static void main(String[] args) {
        List<String> documentList = new ArrayList<>();
        documentList.add("0x91a32d675b238f38dbb0d0ff41a9862ef9a77d74f1584e7929bc285a6c68049d");
        documentList.add("0x74168a2bdcceebd01b84aeff1d9b1e29a1b5980e4e1a5c057ae9f123d6bc2a30");
        documentList.add("0x4e076a71f8826023c76fe48f2d9709fe0bc7a018f4f0195c64f74b9f058da787");
        documentList.add("0x264c5c6832b6b9a81359da49975ce4e88ef2a45f7fd23f4d7b2f2f4fe6e5978d");
        documentList.add("0x2f0e2bc1bc05727fc4b36e38d54ee23f6b2f5023d13eebf72d19d93f4dc3b594");
        documentList.add("0x1b5822a9a6d774e64f9eb03a6a44d3bbf46ff93d301ff305a89998a6b44cc2a2");
        documentList.add("0x8cf87b43efad6af512a4d918b794926ff05d0c6f937bb03ebfde0b3a56b7d3e1");
        documentList.add("0x648ae67e132c8937fcf59d28369c9f96024c28be82023e13769d0c243a497199");
        documentList.add("0xe63fcd2d0e3fe5d3d18dbd2d240ad7f9a0d3d55b9748e58856de789c1c6c1d14");
        documentList.add("0xb24d80944f3f83b9b9d6e0d5bcde99ee6c71e08edc38a4de7275049dc7f62b68");

        MerkleTree merkleTree = new MerkleTree(documentList, "");
        System.out.println("Root: " + merkleTree.getRoot());
    }
}
